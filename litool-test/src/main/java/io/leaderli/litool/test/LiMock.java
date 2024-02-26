package io.leaderli.litool.test;

import io.leaderli.litool.core.collection.ArrayEqual;
import io.leaderli.litool.core.collection.ArrayUtils;
import io.leaderli.litool.core.meta.Either;
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
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class LiMock {
    public static final Map<Class<?>, byte[]> originClasses = new HashMap<>();
    private static final Set<Method> recordMethodCall = new HashSet<>();
    public static final ByteBuddy byteBuddy = new ByteBuddy();
    public static Instrumentation instrumentation = ByteBuddyAgent.install();
    public static ClassPool classPool = ClassPool.getDefault();

    static {
        classPool.importPackage("io.leaderli.litool.test.MockMethodInvoker");
        classPool.importPackage("io.leaderli.litool.core.meta.Either");
    }


    private static void backup(Class<?> clazz) throws IOException, CannotCompileException {
        if (!originClasses.containsKey(clazz)) {
            originClasses.put(clazz, toBytecode(getCtClass(clazz)));
        }
    }

    private static byte[] toBytecode(CtClass ct) throws IOException, CannotCompileException {
        byte[] bytecode = ct.toBytecode();
        ct.defrost();
        return bytecode;
    }

    private static void detach(Class<?> clazz) {

        try {
            getCtClass(clazz).detach();
            if (originClasses.containsKey(clazz)) {
                instrumentation.redefineClasses(new ClassDefinition(clazz, originClasses.get(clazz)));
            }
        } catch (ClassNotFoundException | UnmodifiableClassException e) {
            throw new RuntimeException(e);
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


    private static Method[] findDeclaredMethods(Class<?> clazz, Function<Method, Boolean> methodFilter) {
        return Lira.of(clazz.getDeclaredMethods())
                .filter(methodFilter)
                .filter(m -> !m.isSynthetic())
                .filter(m -> !ModifierUtil.isAbstract(m))
                .toArray(Method.class);
    }


    /**
     * 当类已经加载后，对静态代码块的修改无法在生效，因此仅允许在静态代码块中去调用
     */
    public static void skipClassInitializer(Class<?> mockClass) {
        MethodUtil.onlyCallByCLINIT();
        try {
            backup(mockClass);
            CtClass ct = getCtClass(mockClass);
            CtConstructor classInitializer = ct.makeClassInitializer();
            classInitializer.setBody("{}");
            instrumentation.redefineClasses(new ClassDefinition(mockClass, toBytecode(ct)));
            Class.forName(mockClass.getName(), true, mockClass.getClassLoader());
            detach(mockClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void skipClassConstructors(Class<?> mockClass) {
        skipClassConstructors(mockClass, true);
    }

    /**
     * 一定会重置类
     *
     * @param mockClass mock的类
     */
    public static void skipClassConstructors(Class<?> mockClass, boolean detach) {
        if (mockClass.isInterface() || mockClass.isArray() || mockClass.isEnum() || mockClass == Object.class) {
            return;
        }
        try {
            backup(mockClass);
            if (detach) {
                detach(mockClass);
            }
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
            instrumentation.redefineClasses(new ClassDefinition(mockClass, toBytecode(ct)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param mockClass        代理类，仅代理属于该类的方法，不涉及其他方法
     * @param mockMethodFilter 代理方法的过滤类，用于筛选需要代理的方法
     * @param methodProxy      代理函数
     * @param detach           是否先重置类
     * @see MockMethodInvoker#invoke(String, Class, String, Class[], Object[])
     * @see MethodProxy#apply(Method, Object[]) 根据返回值来判断是否需要真正拦截，如果返回的是 判断是否有右值，
     * 如果有则返回右值，否则不进行拦截。
     * 函数如果判断不需要拦截直接返回{@link Either#none()}即可。如果方法本身返回的是{@link  Either}需要额外对在函数中额外包一层{@link  Either}。
     */
    public static void mock(Class<?> mockClass, Function<Method, Boolean> mockMethodFilter, MethodProxy methodProxy, boolean detach) {
        try {
            backup(mockClass);
            if (detach) {
                detach(mockClass);
            }
            CtClass ct = getCtClass(mockClass);

            for (Method method : findDeclaredMethods(mockClass, mockMethodFilter)) {

                CtMethod ctMethod = getCtMethod(method, ct);
                String uuid = method.getName() + " " + UUID.randomUUID();
                MockMethodInvoker.invokers.put(uuid, LiTuple.of(methodProxy, method));
                String src = StrSubstitution.format2("Either either = MockMethodInvoker.invoke(#uuid#,$class,#name#,$sig,$args);\r\n"
                                + "if(either.isRight()) return ($r)either.getRight();",
                        "#", "#",
                        StringUtils.wrap(uuid, '"'), StringUtils.wrap(method.getName(), '"')
                );
                if (ctMethod.isEmpty()) { // 接口或者抽象方法
                    continue;
                }
                ctMethod.insertBefore(src);
            }

            instrumentation.redefineClasses(new ClassDefinition(mockClass, toBytecode(ct)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * 重置类并拦截方法
     *
     * @see #mock(Class, Function, MethodProxy, boolean)
     */
    public static void mock(Class<?> mockClass, Function<Method, Boolean> mockMethodFilter, MethodProxy methodProxy) {
        mock(mockClass, mockMethodFilter, methodProxy, true);
    }

    /**
     * 重置类并拦截静态方法
     *
     * @see #mockStatic(Class, Function, MethodProxy, boolean)
     */
    public static void mockStatic(Class<?> mockClass, Function<Method, Boolean> mockMethodFilter, MethodProxy methodProxy) {
        mock(mockClass, m -> ModifierUtil.isStatic(m) && mockMethodFilter.apply(m), methodProxy);
    }

    /**
     * 拦截静态方法
     *
     * @see #mock(Class, Function, MethodProxy, boolean)
     */
    public static void mockStatic(Class<?> mockClass, Function<Method, Boolean> mockMethodFilter, MethodProxy methodProxy, boolean detach) {
        mock(mockClass, m -> ModifierUtil.isStatic(m) && mockMethodFilter.apply(m), methodProxy, detach);
    }


    /**
     * 根据筛选器来代理满足条件的方法,断言方法的调用参数、返回值满足条件
     *
     * @param mockClass        记录类
     * @param mockMethodFilter 方法过滤器
     * @param methodAssert     断言函数
     * @see MockMethodInvoker#record(String, Object, Object[], Object)
     */
    public static void record(Class<?> mockClass, Function<Method, Boolean> mockMethodFilter, MethodAssert methodAssert) {

        CtClass ct = getCtClass(mockClass);


        try {
            for (Method method : findDeclaredMethods(mockClass, mockMethodFilter)) {

                CtMethod ctMethod = getCtMethod(method, ct);
                String uuid = method.getName() + " " + UUID.randomUUID();
                MockMethodInvoker.recorders.put(uuid, LiTuple.of(methodAssert, method));
                ctMethod.insertAfter(StrSubstitution.format2("MockMethodInvoker.record( #uuid#,#this#,$args,($w)$_);", "#", "#", StringUtils.wrap(uuid, '"'), ModifierUtil.isStatic(method) ? "null" : "$0"));

            }
            instrumentation.redefineClasses(new ClassDefinition(mockClass, toBytecode(ct)));
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

    public static void assertMethodCalled() {
        Assertions.assertTrue(recordMethodCall.isEmpty(), "the method is not called: " + recordMethodCall);
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

    public static Recorder recorder(Class<?> mockClass) {
        return new Recorder(mockClass);
    }

    public static Mocker mocker(Class<?> mockClass) {
        return new Mocker(mockClass, true);
    }

    public static Mocker mocker(Class<?> mockClass, boolean reset) {
        return new Mocker(mockClass, reset);
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    public static class Mocker {

        private final Class<?> mockClass;
        private final Map<Method, MethodValue> methodValueMap = new HashMap<>();
        private Method currentMethod;
        private ArrayEqual currentArgs;
        private final boolean detach;
        private boolean build;

        private Mocker(Class<?> mockClass, boolean detach) {
            this.mockClass = mockClass;
            this.detach = detach;
            // 仅在build过程中生效，用于记录方法的调用
            mock(mockClass, m -> true, (method, args) -> {
                if (build) {
                    return Either.none();
                }
                currentMethod = method;
                currentArgs = ArrayEqual.of(args);
                methodValueMap.put(currentMethod, new MethodValue(currentMethod));
                return PrimitiveEnum.get(method.getReturnType()).zero_value;
            }, false);
        }


        public <T> Mocker when(T call, T result) {
            if (currentMethod == null) {
                throw new IllegalStateException("method may abstruct, it's not support");
            }
            methodValueMap.get(currentMethod).whenValue.put(currentArgs, result);

            currentMethod = null;
            currentArgs = null;
            return this;
        }

        public <T> Mocker when(T call, T result, T other) {
            return when(call, result, (m, args) -> other);
        }

        public <T> Mocker when(T call, BiFunction<Method, Object[], T> otherValue) {
            if (currentMethod == null) {
                throw new IllegalStateException("method may abstruct, it's not support");
            }
            methodValueMap.get(currentMethod).otherValue = otherValue;

            currentMethod = null;
            currentArgs = null;
            return this;
        }

        public <T> Mocker when(T call, T result, BiFunction<Method, Object[], T> otherValue) {
            if (currentMethod == null) {
                throw new IllegalStateException("method may abstruct, it's not support");
            }
            methodValueMap.get(currentMethod).whenValue.put(currentArgs, result);
            methodValueMap.get(currentMethod).otherValue = otherValue;

            currentMethod = null;
            currentArgs = null;
            return this;
        }

        public void build() {

            build = true;
            mock(mockClass, methodValueMap::containsKey, (m, args) -> {
                        MethodValue methodValue = methodValueMap.get(m);
                        ArrayEqual<Object> key = ArrayEqual.of(args);
                        if (methodValue.whenValue.containsKey(key)) {
                            return methodValue.whenValue.get(key);
                        }
                        if (methodValue.otherValue == null) {
                            return Either.none();
                        }
                        return methodValue.otherValue.apply(m, args);
                    }
                    , detach);
        }


    }

    public static class MethodValue<T, R> {
        final Method method;
        final Map<ArrayEqual<T>, R> whenValue = new HashMap<>();
        BiFunction<Method, Object[], R> otherValue;

        MethodValue(Method method) {
            this.method = method;
        }
    }

    public static class Recorder {
        private final Class<?> mockClass;
        private final Map<Method, List<MethodAssert>> methodAsserts = new HashMap<>();
        private Method currentMethod;
        private boolean build;

        public Recorder(Class<?> mockClass) {
            this.mockClass = mockClass;
            // 仅在build过程中生效，用于记录方法的调用
            mock(mockClass, m -> true, (method, args) -> {
                if (build) {
                    return Either.none();
                }
                currentMethod = method;
                methodAsserts.put(method, new ArrayList<>());
                return PrimitiveEnum.get(method.getReturnType()).zero_value;
            }, false);
        }

        public Recorder when(Object object) {
            return this;
        }

        public Recorder when(Runnable runnable) {
            runnable.run();
            return this;
        }

        public Recorder called() {
            recordMethodCall.add(currentMethod);
            methodAsserts.get(currentMethod).add((method, _this, args, _return) -> {
                recordMethodCall.remove(method);
            });
            return this;
        }


        public Recorder arg(int index, Object arg) {
            methodAsserts.get(currentMethod).add((method, _this, args, _return) -> {
                Assertions.assertTrue(args.length > index);
                Assertions.assertEquals(arg, args[index]);
            });
            return this;
        }

        public Recorder args(Object... compareArgs) {
            methodAsserts.get(currentMethod).add((method, _this, args, _return) -> {
                Assertions.assertArrayEquals(compareArgs, args);
            });
            return this;
        }

        public Recorder assertReturn(Object compareReturn) {
            methodAsserts.get(currentMethod).add((method, _this, args, _return) -> {
                Assertions.assertEquals(compareReturn, _return);
            });
            return this;
        }

        public void build() {

            build = true;
            record(mockClass, m -> !methodAsserts.getOrDefault(m, new ArrayList<>()).isEmpty(),
                    (m, _this, args, _return) -> methodAsserts.get(m).forEach(methodAssert -> methodAssert.apply(m, _this, args, _return)));
        }

    }
}
