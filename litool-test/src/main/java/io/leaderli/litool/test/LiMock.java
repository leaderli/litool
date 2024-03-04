package io.leaderli.litool.test;

import io.leaderli.litool.core.collection.ArrayUtils;
import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.Either;
import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.text.StrSubstitution;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.core.type.*;
import javassist.*;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import org.junit.jupiter.api.Assertions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LiMock {
    public static final Map<Class<?>, byte[]> originClasses = new HashMap<>();
    public static final ByteBuddy byteBuddy = new ByteBuddy();
    public static Instrumentation instrumentation = ByteBuddyAgent.install();
    public static ClassPool classPool = ClassPool.getDefault();
    public final static boolean jacoco;

    static {
        jacoco = checkJacoco();
        classPool.importPackage("io.leaderli.litool.test.MockMethodInvoker");
        classPool.importPackage("io.leaderli.litool.core.meta.Either");
    }

    private static boolean checkJacoco() {
        for (Class<?> loadedClass : instrumentation.getAllLoadedClasses()) {
            if (ClassFileTransformer.class.isAssignableFrom(loadedClass)) {
                if (loadedClass.getName().startsWith("org.jacoco.agent.rt")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static CtClass getCtClass(Class<?> clazz) {
        try {
            if (jacoco && instrumentation.isModifiableClass(clazz)) {
                instrumentation.addTransformer(new TempClassFileTransformer(instrumentation), true);
                instrumentation.retransformClasses(clazz);
            }

            return classPool.getCtClass(clazz.getName());
        } catch (Exception e) {
            throw new RuntimeException(clazz + " : " + e);
        }
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

    public static void detach(Class<?> clazz) throws UnmodifiableClassException, ClassNotFoundException {


        getCtClass(clazz).detach();
        instrumentation.retransformClasses(clazz);

        if (originClasses.containsKey(clazz)) {
            instrumentation.redefineClasses(new ClassDefinition(clazz, originClasses.get(clazz)));
        }

    }

    static class TempClassFileTransformer implements ClassFileTransformer {
        final Instrumentation instrumentation;

        TempClassFileTransformer(Instrumentation instrumentation) {
            this.instrumentation = instrumentation;
        }

        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            try {
                CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
            } catch (IOException ignore) {
            }
            instrumentation.removeTransformer(this);
            return null;
        }
    }

    private static CtMethod getCtMethod(Method method, CtClass ct) throws NotFoundException {
        CtClass[] params = ArrayUtils.map(method.getParameterTypes(), CtClass.class, LiMock::getCtClass);
        return ct.getDeclaredMethod(method.getName(), params);
    }


    private static Method[] findDeclaredMethods(Class<?> clazz, MethodFilter methodFilter) {
        methodFilter.addHead(m -> !(m.isSynthetic() || ModifierUtil.isAbstract(m)));
        return Lira.of(clazz.getDeclaredMethods())
                .filter(methodFilter)
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
     * @param mockClass    代理类，仅代理属于该类的方法，不涉及其他方法
     * @param methodFilter 代理方法的过滤类，用于筛选需要代理的方法
     * @param methodProxy  代理函数
     * @param detach       是否先重置类
     * @see MockMethodInvoker#invoke(String, Class, String, Class[], Object[])
     * @see MethodProxy#apply(Method, Object[]) 根据返回值来判断是否需要真正拦截，如果返回的是 判断是否有右值，
     * 如果有则返回右值，否则不进行拦截。
     * 函数如果判断不需要拦截直接返回{@link Either#none()}即可。如果方法本身返回的是{@link  Either}需要额外对在函数中额外包一层{@link  Either}。
     */
    public static void mock(Class<?> mockClass, MethodFilter methodFilter, MethodProxy methodProxy, boolean detach) {
        try {
            backup(mockClass);
            if (detach) {
                detach(mockClass);
            }
            CtClass ct = getCtClass(mockClass);

            for (Method method : findDeclaredMethods(mockClass, methodFilter)) {

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
     * @see #mock(Class, MethodFilter, MethodProxy, boolean)
     */
    public static void mock(Class<?> mockClass, MethodFilter methodFilter, MethodProxy methodProxy) {
        mock(mockClass, methodFilter, methodProxy, true);
    }

    /**
     * 重置类并拦截静态方法
     *
     * @see #mockStatic(Class, MethodFilter, MethodProxy, boolean)
     */
    public static void mockStatic(Class<?> mockClass, MethodFilter methodFilter, MethodProxy methodProxy) {
        mock(mockClass, methodFilter.addHead(ModifierUtil::isStatic), methodProxy);
    }

    /**
     * 拦截静态方法
     *
     * @see #mock(Class, MethodFilter, MethodProxy, boolean)
     */
    public static void mockStatic(Class<?> mockClass, MethodFilter methodFilter, MethodProxy methodProxy, boolean detach) {
        mock(mockClass, methodFilter.addHead(ModifierUtil::isStatic), methodProxy, detach);
    }


    /**
     * 根据筛选器来代理满足条件的方法,断言方法的调用参数、返回值满足条件
     *
     * @param mockClass        记录类
     * @param mockMethodFilter 方法过滤器
     * @param methodAssert     断言函数
     * @see MockMethodInvoker#record(String, Object, Object[], Object)
     */
    public static void record(Class<?> mockClass, MethodFilter mockMethodFilter, MethodAssert methodAssert) {

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
     * @see #record(Class, MethodFilter, MethodAssert)
     */
    public static void recordStatic(Class<?> mockClass, MethodFilter methodFilter, MethodAssert methodAssert) {
        record(mockClass, methodFilter.addHead(ModifierUtil::isStatic), methodAssert);
    }

    /**
     * 断言方法被调用
     *
     * @see Recorder#called()
     */
    public static void assertMethodCalled() {
        for (Method method : Recorder.recordMethodCall) {
            Assertions.assertTrue(Recorder.actualMethodCall.contains(method), "the method is not called: " + method.getName());
        }
    }

    public static void assertDoesNotThrow() {
        for (Throwable throwable : Recorder.assertThrow) {
            throw new RuntimeException(throwable);
        }
    }

    /**
     * 情况方法调用记录
     *
     * @see Recorder#called()
     */
    public static void clearMethodCalled() {
        Recorder.actualMethodCall.clear();
    }

    public static void reset() {
        Recorder.assertThrow.clear();
        originClasses.forEach((k, v) -> {
            try {
                detach(k);
                instrumentation.redefineClasses(new ClassDefinition(k, v));
            } catch (ClassNotFoundException | UnmodifiableClassException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static Recorder recorder(Class<?> mockClass) {
        return new Recorder(mockClass);
    }

    public static <T> RecordBean<T> recordBean(Class<T> mockClass) {
        return new RecordBean<>(mockClass);
    }

    public static Mocker mocker(Class<?> mockClass) {
        return new Mocker(mockClass, true);
    }

    /**
     * @param mockClass 模拟类
     * @param detach    是否重置之前变动
     * @return 一个模拟的记录类
     */
    public static Mocker mocker(Class<?> mockClass, boolean detach) {
        return new Mocker(mockClass, detach);
    }


    /**
     * 方法记录自动带上实例
     */
    public static <T> MockBeanInterface<MockBean<T>, T> mockerBean(Class<T> mockClass) {
        return new MockBean<>(mockClass, true);
    }

    /**
     * 方法记录自动带上实例
     *
     * @param mockClass class
     * @param detach    是否重置之前变动
     */
    public static <T> MockBeanInterface<MockBean<T>, T> mockerBean(Class<T> mockClass, boolean detach) {
        return new MockBean<>(mockClass, detach);
    }

    /**
     * 方法记录自动带上实例
     */
    @SafeVarargs
    public static <T> MockBeanInterface<MockBeans<T>, T> mockerBeans(Class<? extends T>... mockClass) {
        return new MockBeans<>(mockClass);
    }

    /**
     * 方法记录自动带上实例
     */
    public static <T> MockInterface<T> mockerInterface(Class<T> mockClass) {
        LiAssertUtil.assertTrue(mockClass.isInterface(), "only support interface");
        return new MockInterface<>(mockClass);
    }

    @SuppressWarnings("unchecked")
    public static <T> MockInterface<T> mockerInterface(LiTypeToken<T> mockClass) {
        LiAssertUtil.assertTrue(mockClass.getRawType().isInterface(), "only support interface");
        return new MockInterface<>((Class<T>) (mockClass.getRawType()));
    }


}
