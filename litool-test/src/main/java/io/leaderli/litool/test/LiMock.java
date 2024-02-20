package io.leaderli.litool.test;

import io.leaderli.litool.core.collection.ArrayEqual;
import io.leaderli.litool.core.collection.ArrayUtils;
import io.leaderli.litool.core.meta.LiTuple;
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
import java.util.HashMap;
import java.util.Map;
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
    }

    /**
     * 当类已经加载后，对静态代码块的修改无法在生效，因此仅允许在静态代码块中去调用
     */
    public static void skipClassInitializer(Class<?> mockClass) {
        MethodUtil.onlyCallByCLINIT();
        try {

            CtClass ct = backupOrRestore(mockClass);
            CtConstructor classInitializer = ct.makeClassInitializer();
            classInitializer.setBody("{}");
            redefineClasses(mockClass, ct);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void redefineClasses(Class<?> mockClass, CtClass ct) throws IOException, CannotCompileException, UnmodifiableClassException, ClassNotFoundException {
        byte[] bytecode = ct.toBytecode();
        ct.defrost();
        instrumentation.redefineClasses(new ClassDefinition(mockClass, bytecode));
    }


    private static CtClass backupOrRestore(Class<?> clazz) throws IOException, CannotCompileException {

        CtClass ct = getCtClass(clazz);
        if (originClasses.containsKey(clazz)) {
            byte[] bytes = originClasses.get(clazz);
            try {
                instrumentation.redefineClasses(new ClassDefinition(clazz, bytes));
                ct.detach(); // 恢复初始状态
            } catch (ClassNotFoundException | UnmodifiableClassException e) {
                throw new RuntimeException(e);
            }
            return ct;
        }
        byte[] bytecode = ct.toBytecode();
        ct.defrost();

        originClasses.putIfAbsent(clazz, bytecode);
        return ct;
    }

    public static void mockStatic(Class<?> mockClass, Function<Method, Boolean> mockMethodFilter, StaticMethodProxy staticMethodProxy) {
        mock(mockClass, m -> ModifierUtil.isStatic(m) && mockMethodFilter.apply(m), staticMethodProxy);
    }

    static CtClass getCtClass(Class<?> clazz) {
        try {
            return classPool.getCtClass(clazz.getName());
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据筛选器来代理满足条件的静态方法,将方法由 {@link  StaticMethodProxy} 去执行。
     * 可以通过{@link  StaticMethodProxy#when(Method, Object[])}来根据参数来决定是否需要最终由
     * {@link  StaticMethodProxy#apply(Method, Object[])}代理执行
     *
     * @param mockClass         代理类，仅代理属于该类的方法，不涉及其他方法
     * @param mockMethodFilter  代理方法的过滤类，用于筛选需要代理的方法
     * @param staticMethodProxy 代理函数
     */
    static void mock(Class<?> mockClass, Function<Method, Boolean> mockMethodFilter, StaticMethodProxy staticMethodProxy) {
        try {

            CtClass ct = backupOrRestore(mockClass);

            for (Method method : mockClass.getDeclaredMethods()) {
                if (method.isSynthetic() || !mockMethodFilter.apply(method)) {
                    continue;
                }

                CtClass[] params = ArrayUtils.map(method.getParameterTypes(), CtClass.class, LiMock::getCtClass);
                CtMethod ctMethod = ct.getDeclaredMethod(method.getName(), params);

                LiTuple<StaticMethodProxy, Method> invokerTuple = LiTuple.of(staticMethodProxy, method);
                String invokerName = invokerTuple.toString();
                MockMethodInvoker.invokers.put(invokerName, invokerTuple);
                String src = StrSubstitution.format2("java.util.function.Supplier staticMethodProxy = MockMethodInvoker.getInvoke($class,#id#,#name#,$sig,$args);\r\n"
                                + "if( staticMethodProxy!=null) return ($r)staticMethodProxy.get();",
                        "#", "#",
                        StringUtils.wrap(invokerName, '"'), StringUtils.wrap(method.getName(), '"')
                );
                ctMethod.insertBefore(src);
            }

            redefineClasses(mockClass, ct);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void reset() {
        originClasses.forEach((k, v) -> {
            try {
                getCtClass(k).detach();
                instrumentation.redefineClasses(new ClassDefinition(k, v));
            } catch (ClassNotFoundException | UnmodifiableClassException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static When builder(Class<?> mockClass) {
        return new Builder(mockClass);
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
    public static class Builder implements WhenOther, Then {

        private final Class<?> mockClass;
        private final Map<Method, MethodValue> methodValueMap = new HashMap<>();
        private Method currenMethod;
        private ArrayEqual currentArgs;

        public Builder(Class<?> mockClass) {
            this.mockClass = mockClass;
            mock(mockClass, m -> true, (method, args) -> {
                // 用以记录当前调用的方法，参数，以供后续打桩使用
                currenMethod = method;
                currentArgs = ArrayEqual.of(args);
                methodValueMap.put(currenMethod, new MethodValue(currenMethod));

                return PrimitiveEnum.get(method.getReturnType()).zero_value;
            });
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
            mock(mockClass, methodValueMap::containsKey, (m, args) -> {
                MethodValue methodValue = methodValueMap.get(m);
                ArrayEqual<Object> key = ArrayEqual.of(args);
                if (methodValue.whenValue.containsKey(key)) {
                    return methodValue.whenValue.get(key);
                }
                return methodValue.otherValue.apply(m, args);
            });
        }

        public static class MethodValue<T, R> {
            final Method method;
            BiFunction<Method, Object[], R> otherValue = (m, args) -> null;
            Map<ArrayEqual<T>, R> whenValue = new HashMap<>();

            MethodValue(Method method) {
                this.method = method;
            }
        }
    }
}
