package io.leaderli.litool.test;

import io.leaderli.litool.core.collection.ArrayEqual;
import io.leaderli.litool.core.collection.ArrayUtils;
import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.text.StrSubstitution;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.core.type.MethodUtil;
import io.leaderli.litool.core.type.ModifierUtil;
import io.leaderli.litool.core.type.PrimitiveEnum;
import javassist.*;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;

import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class LiMock {
    public static final Map<Class<?>, byte[]> originClasses = new HashMap<>();
    public static final ByteBuddy byteBuddy = new ByteBuddy();
    public static Instrumentation instrumentation = ByteBuddyAgent.install();
    public static ClassPool classPool = ClassPool.getDefault();

    static {
        classPool.importPackage("io.leaderli.litool.test.MockMethodInvoker");
        classPool.importPackage("io.leaderli.litool.test.StaticMethodInvoker");
        classPool.importPackage("java.util.function.Supplier");
    }


    private static void redefineClasses(Class<?> mockClass, CtClass ct, boolean detach) throws IOException, CannotCompileException, UnmodifiableClassException, ClassNotFoundException {
        byte[] bytes = ct.toBytecode();
        instrumentation.redefineClasses(new ClassDefinition(mockClass, bytes));
        if (detach) {
            ct.detach();
        } else {
            ct.defrost();
        }
    }


    private static void backupOrReset(Class<?> clazz) throws IOException, CannotCompileException {

        CtClass ct = getCtClass(clazz);
        if (originClasses.containsKey(clazz)) {
            byte[] bytes = originClasses.get(clazz);
            try {
                instrumentation.redefineClasses(new ClassDefinition(clazz, bytes));
            } catch (ClassNotFoundException | UnmodifiableClassException e) {
                throw new RuntimeException(e);
            }
            ct.detach();
        } else {
            byte[] bytes = ct.toBytecode();
            ct.defrost();
            originClasses.put(clazz, bytes);
        }
    }

    private static CtClass getCtClass(Class<?> clazz) {
        try {
            return classPool.getCtClass(clazz.getName());
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static CtMethod getCtMethod(Method method, CtClass ct) throws NotFoundException {
        CtClass[] params = ArrayUtils.map(method.getParameterTypes(), CtClass.class, LiMock::getCtClass);
        return ct.getDeclaredMethod(method.getName(), params);
    }


    private static Method[] findDeclaredMethods(Class<?> clazz, Function<Method, Boolean> methodfilter) {
        return Lira.of(clazz.getDeclaredMethods())
                .filter(methodfilter)
                .filter(m -> !m.isSynthetic())
                .toArray(Method.class);
    }


    /**
     * 当类已经加载后，对静态代码块的修改无法在生效，因此仅允许在静态代码块中去调用
     */
    public static void skipClassInitializer(Class<?> mockClass) {
        MethodUtil.onlyCallByCLINIT();
        try {
            backupOrReset(mockClass);
            CtClass ct = getCtClass(mockClass);
            CtConstructor classInitializer = ct.makeClassInitializer();
            classInitializer.setBody("{}");
            redefineClasses(mockClass, ct, true);
            Class.forName(mockClass.getName(), true, mockClass.getClassLoader());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void skipClassConstructors(Class<?> mockClass) {
        if (mockClass.isInterface() || mockClass.isArray() || mockClass.isEnum() || mockClass == Object.class) {
            return;
        }
        try {
            backupOrReset(mockClass);
            CtClass ct = getCtClass(mockClass);
            for (CtConstructor constructor : ct.getDeclaredConstructors()) {
                if (constructor.callsSuper()) {
                    // 组装调用super
                    CtClass[] superConstructorParameterTypes = Lira.of(ct.getSuperclass().getDeclaredConstructors())
                            .mapIgnoreError(CtBehavior::getParameterTypes)
                            .sorted(Comparator.comparingInt(p -> p.length)).first().get();

                    String[] parameters = Lira.of(superConstructorParameterTypes)
                            .map(CtClass::getName)
                            .map(PrimitiveEnum::get)
                            .map(e -> String.valueOf(e.zero_value))
                            .toNullableArray(String.class);

                    String src = StrSubstitution.$format("{super(${param});}", String.join(",", parameters));
                    constructor.setBody(src);
                } else {

                    constructor.setBody("{}");
                }
            }
            redefineClasses(mockClass, ct, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据筛选器来代理满足条件的方法,方法由 {@link  MethodProxy} 去执行。
     * 可以通过{@link  MethodProxy#when(Method, Object[])}来根据参数来决定是否需要最终由
     * {@link  MethodProxy#apply(Method, Object[])}代理执行
     *
     * @param mockClass        代理类，仅代理属于该类的方法，不涉及其他方法
     * @param mockMethodFilter 代理方法的过滤类，用于筛选需要代理的方法
     * @param methodProxy      代理函数
     */
    public static void mock(Class<?> mockClass, Function<Method, Boolean> mockMethodFilter, MethodProxy methodProxy) {
        try {
            backupOrReset(mockClass);
            CtClass ct = getCtClass(mockClass);

            for (Method method : findDeclaredMethods(mockClass, mockMethodFilter)) {

                CtMethod ctMethod = getCtMethod(method, ct);
                String uuid = method.getName() + " " + UUID.randomUUID();
                MockMethodInvoker.invokers.put(uuid, LiTuple.of(methodProxy, method));
                String src = StrSubstitution.format2("Supplier supplier= MockMethodInvoker.getInvoke(#uuid#,$class,#name#,$sig,$args);\r\n"
                                + "if( supplier!=null) return ($r)supplier.get();",
                        "#", "#",
                        StringUtils.wrap(uuid, '"'), StringUtils.wrap(method.getName(), '"')
                );
                ctMethod.insertBefore(src);
            }

            redefineClasses(mockClass, ct, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 拦截静态方法
     *
     * @see #mock(Class, Function, MethodProxy)
     */
    public static void mockStatic(Class<?> mockClass, Function<Method, Boolean> mockMethodFilter, MethodProxy methodProxy) {
        mock(mockClass, m -> ModifierUtil.isStatic(m) && mockMethodFilter.apply(m), methodProxy);
    }


    /**
     * 根据筛选器来代理满足条件的方法,断言方法的调用参数、返回值满足条件
     *
     * @param mockClass        记录类
     * @param mockMethodFilter 方法过滤器
     * @param methodAssert     断言函数
     */
    public static void record(Class<?> mockClass, Function<Method, Boolean> mockMethodFilter, MethodAssert methodAssert) {

        CtClass ct = getCtClass(mockClass);


        try {
            for (Method method : findDeclaredMethods(mockClass, mockMethodFilter)) {

                CtMethod ctMethod = getCtMethod(method, ct);
                String uuid = method.getName() + " " + UUID.randomUUID();
                MockMethodInvoker.recorders.put(uuid, methodAssert);
                ctMethod.insertAfter(StrSubstitution.format2("MockMethodInvoker.record( #uuid#,#this#,$args,($w)$_);", "#", "#", StringUtils.wrap(uuid, '"'), ModifierUtil.isStatic(method) ? "null" : "$0"));

            }
            redefineClasses(mockClass, ct, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * 拦截静态方法
     *
     * @see #record(Class, Function, MethodAssert)
     */
    public static void recordStatic(Class<?> mockClass, Function<Method, Boolean> mockMethodFilter, MethodAssert methodAssert) {
        record(mockClass, m -> ModifierUtil.isStatic(m) && mockMethodFilter.apply(m), methodAssert);
    }


    public static void reset() {
        originClasses.forEach((k, v) -> {
            try {
                instrumentation.redefineClasses(new ClassDefinition(k, v));
            } catch (ClassNotFoundException | UnmodifiableClassException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static When mocker(Class<?> mockClass) {
        return new Mocker(mockClass);
    }

    public interface Then extends Other {
        WhenOther then(Object value);
    }


    @SuppressWarnings("rawtypes")
    public interface Other {

        WhenBuild other(BiFunction<Method, Object[], Object> biFunction);

        default WhenBuild other(Supplier supplier) {
            return other((m, args) -> supplier.get());
        }

        default WhenBuild other(Object value) {
            return other((m, args) -> value);
        }
    }

    public interface WhenOther extends WhenBuild, Other {
    }

    public interface When {
        Then when(Object when);
    }

    public interface WhenBuild extends When {
        void build();
    }

    /**
     * 利用不同的接口来控制链式调用
     * <p>
     * eg:
     * <pre>
     *  builder(Bean.class)
     *  .when(bean.m1(1)).then(1)
     *  .when(bean.m1(2)).then(2)
     *  .other(0)
     *  .build()
     * </pre>
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static class Mocker implements WhenOther, Then {

        private final Class<?> mockClass;
        private final Map<Method, MethodValue> methodValueMap = new HashMap<>();
        private Method currenMethod;
        private ArrayEqual currentArgs;

        public Mocker(Class<?> mockClass) {
            this.mockClass = mockClass;
            mock(mockClass, m -> true, (method, args) -> {
                // 用以记录当前调用的方法，参数，以供后续打桩使用
                currenMethod = method;
                currentArgs = ArrayEqual.of(args);
                methodValueMap.put(currenMethod, new MethodValue(currenMethod));

                return PrimitiveEnum.get(method.getReturnType()).zero_value;
            });
        }

        public void ignoreInit() {
        }

        public Then when(Object o) {
            return this;
        }

        public WhenOther then(Object value) {
            methodValueMap.get(currenMethod).whenValue.put(currentArgs, value);
            return this;
        }

        public WhenBuild other(BiFunction<Method, Object[], Object> biFunction) {
            methodValueMap.get(currenMethod).otherValue = biFunction;
            return this;
        }


        public void build() {
            mock(mockClass, methodValueMap::containsKey,
                    new MethodProxy() {
                        @Override
                        public boolean when(Method m, Object[] args) {
                            MethodValue methodValue = methodValueMap.get(m);
                            ArrayEqual<Object> key = ArrayEqual.of(args);
                            return methodValue.whenValue.containsKey(key) || methodValue.otherValue != null;
                        }

                        @Override
                        public Object apply(Method m, Object[] args) {
                            MethodValue methodValue = methodValueMap.get(m);
                            ArrayEqual<Object> key = ArrayEqual.of(args);
                            if (methodValue.whenValue.containsKey(key)) {
                                return methodValue.whenValue.get(key);
                            }
                            return methodValue.otherValue.apply(m, args);
                        }
                    }
            );
        }

        public static class MethodValue<T, R> {
            final Method method;
            final Map<ArrayEqual<T>, R> whenValue = new HashMap<>();
            BiFunction<Method, Object[], R> otherValue;

            MethodValue(Method method) {
                this.method = method;
            }
        }
    }
}
