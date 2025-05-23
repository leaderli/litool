package io.leaderli.litool.test;

import io.leaderli.litool.core.collection.ArrayUtils;
import io.leaderli.litool.core.exception.LiAssertUtil;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.function.Consumer;

public class LiMock {
    private LiMock() {
    }

    public static class Skip {
        private Skip() {
        }
    }

    /**
     * 用于标记跳过mock
     */
    public static final Skip SKIP_MARK = new Skip();


    protected static final Map<Class<?>, byte[]> originClasses = new HashMap<>();
    public static final ByteBuddy byteBuddy = new ByteBuddy();
    public static final Instrumentation instrumentation = ByteBuddyAgent.install();
    public static final ClassPool classPool = ClassPool.getDefault();
    public static final boolean RUN_IN_JACOCO;

    static {
        RUN_IN_JACOCO = checkJacoco();
        classPool.importPackage("io.leaderli.litool.test.MethodValueFactory");
        classPool.importPackage("io.leaderli.litool.core.meta.Either");
    }

    /**
     * 判断当时是否处于jacoco执行环境
     */
    private static boolean checkJacoco() {
        for (Class<?> loadedClass : instrumentation.getAllLoadedClasses()) {
            if (ClassFileTransformer.class.isAssignableFrom(loadedClass) && loadedClass.getName().startsWith("org.jacoco.agent.rt")) {
                return true;
            }

        }
        return false;
    }

    public static CtClass getCtClass(Class<?> clazz) {
        try {
            if (RUN_IN_JACOCO && instrumentation.isModifiableClass(clazz)) {
                instrumentation.addTransformer(new TempClassFileTransformer(instrumentation), true);
                instrumentation.retransformClasses(clazz);
            }

            return classPool.getCtClass(clazz.getName());
        } catch (Exception e) {
            throw new MockException(clazz + " : " + e);
        }
    }


    public static void backup(Class<?> clazz) throws IOException, CannotCompileException {
        if (!originClasses.containsKey(clazz)) {
            originClasses.put(clazz, toBytecode(getCtClass(clazz)));
        }
    }

    public static byte[] toBytecode(CtClass ct) throws IOException, CannotCompileException {
        byte[] bytecode = ct.toBytecode();
        ct.defrost();
        return bytecode;
    }

    static class TempClassFileTransformer implements ClassFileTransformer {
        final Instrumentation instrumentation;

        TempClassFileTransformer(Instrumentation instrumentation) {
            this.instrumentation = instrumentation;
        }

        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            try {
                classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
            } catch (IOException ignore) {
                // ignore
            }
            instrumentation.removeTransformer(this);
            return new byte[0];
        }
    }

    public static CtMethod getCtMethod(Method method, CtClass ct) throws NotFoundException {
        CtClass[] params = ArrayUtils.map(method.getParameterTypes(), CtClass.class, LiMock::getCtClass);
        return ct.getDeclaredMethod(method.getName(), params);
    }


    public static Method[] findDeclaredMethods(Class<?> clazz, MethodFilter methodFilter) {
        methodFilter.addHead(m -> !(m.isSynthetic() || ModifierUtil.isAbstract(m)));
        return Lira.of(clazz.getDeclaredMethods())
                .filter(methodFilter)
                .toArray(Method.class);
    }


    /**
     * 当类已经加载后，对静态代码块的修改无法在生效，因此仅允许在静态代码块中去调用
     */
    public static synchronized void skipClassInitializer(Class<?> mockClass) {
        MethodUtil.onlyCallByCLINIT();

        try {
            backup(mockClass);
            CtClass ct = getCtClass(mockClass);
            CtConstructor classInitializer = ct.makeClassInitializer();
            classInitializer.setBody("{}");
            instrumentation.redefineClasses(new ClassDefinition(mockClass, toBytecode(ct)));
            Class.forName(mockClass.getName(), true, mockClass.getClassLoader());
            MethodValueFactory.detach(mockClass);
        } catch (Exception e) {
            throw new MockException(e);
        }
    }

    public static synchronized void skipClassConstructors(Class<?> mockClass) {


        MethodUtil.onlyCallByCLINIT();
        if (mockClass.isInterface() || mockClass.isArray() || mockClass.isEnum() || mockClass == Object.class) {
            return;
        }
        try {
            backup(mockClass);
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
            throw new MockException(e);
        }
    }

    /**
     * @param mockClass    代理类，仅代理属于该类的方法，不涉及其他方法
     * @param methodFilter 代理方法的过滤类，用于筛选需要代理的方法
     * @param methodProxy  代理函数
     * @param detach       是否先重置类
     * @see MethodValueFactory#invoke(Class, String, Object, Object[])
     * @see MethodProxy#apply(Method, Object[]) 根据返回值来判断是否需要真正拦截，不需要拦截直接返回{@link #SKIP_MARK}即可，其他表示拦截
     */
    public static void mock(Class<?> mockClass, MethodFilter methodFilter, MethodProxy<?> methodProxy, boolean detach) {
        MethodValueFactory.ClassMock classMock = MethodValueFactory.computeIfAbsent(mockClass);
        if (detach) {
            classMock.detach();
        }

        classMock.putMethodProxy(methodFilter, methodProxy);
    }


    /**
     * 重置类并拦截方法
     *
     * @see #mock(Class, MethodFilter, MethodProxy, boolean)
     */
    public static void mock(Class<?> mockClass, MethodFilter methodFilter, MethodProxy<?> methodProxy) {
        mock(mockClass, methodFilter, methodProxy, true);
    }

    /**
     * 重置类并拦截方法
     *
     * @see #mock(Class, Class, boolean)
     */
    public static void mock(Class<?> mockClass, Class<?> delegate) {
        mock(mockClass, delegate, true);
    }

    /**
     * 通过代理类的方式去代理执行，代理类中需要全部为静态方法，且需要为 public。
     * 代理方法的参数类型与原方法类型一致，名称一致，返回类型一致
     */
    public static void mock(Class<?> mockClass, Class<?> delegate, boolean detach) {
        Map<Method, Method> delegateMap = new HashMap<>();
        for (Method delegateMethod : Lira.of(delegate.getMethods())
                .filter(ModifierUtil::isStatic)) {

            for (Method method : findDeclaredMethods(mockClass, MethodFilter
                    .name(delegateMethod.getName())
                    ._parameterType(delegateMethod.getParameterTypes())
                    ._return(delegateMethod.getReturnType()))) {
                delegateMap.put(method, delegateMethod);
            }

        }
        mock(mockClass, MethodFilter.of(delegateMap::containsKey), (method, args) -> delegateMap.get(method).invoke(args), detach);
    }


    /**
     * 重置类并拦截静态方法
     *
     * @see #mockStatic(Class, MethodFilter, MethodProxy, boolean)
     */
    public static void mockStatic(Class<?> mockClass, MethodFilter
            methodFilter, MethodProxy<?> methodProxy) {
        mock(mockClass, methodFilter.addHead(ModifierUtil::isStatic), methodProxy);
    }

    /**
     * 拦截静态方法
     *
     * @see #mock(Class, MethodFilter, MethodProxy, boolean)
     */
    public static void mockStatic(Class<?> mockClass, MethodFilter
            methodFilter, MethodProxy<?> methodProxy, boolean detach) {
        mock(mockClass, methodFilter.addHead(ModifierUtil::isStatic), methodProxy, detach);
    }


    /**
     * 根据筛选器来代理满足条件的方法,断言方法的调用参数、返回值满足条件
     *
     * @param mockClass    记录类
     * @param methodFilter 方法过滤器
     * @param methodAssert 断言函数
     */
    public static void record(Class<?> mockClass, MethodFilter methodFilter, MethodAssert methodAssert) {

        MethodValueFactory.ClassMock classMock = MethodValueFactory.computeIfAbsent(mockClass);
        classMock.putMethodAssert(methodFilter, methodAssert);
    }


    /**
     * 通过代理类的方式去记录，代理类中需要全部为静态方法，且需要为 public。
     * 代理方法与原方法名称一致，返回类型void，第一个参数、第二个参数为被代理的实例和被代理方法的返回值。其余参数和原方法类型保持一致
     */
    public static void record(Class<?> mockClass, Class<?> delegate) {

        Map<Method, Method> delegateMap = new HashMap<>();
        for (Method delegateMethod : Lira.of(delegate.getMethods())
                .filter(m -> m.getReturnType() == void.class && ModifierUtil.isStatic(m))) {

            MethodFilter methodFilter = MethodFilter
                    .name(delegateMethod.getName())
                    ._of(m -> {
                        // 代理方法第一个参数、第二个参数为 this 和 return
                        Class<?>[] compare = ArrayUtils.insert(m.getParameterTypes(), 0, m.getReturnType());
                        return Arrays.equals(delegateMethod.getParameterTypes(), compare);
                    });
            for (Method method : findDeclaredMethods(mockClass, methodFilter)) {
                delegateMap.put(method, delegateMethod);
            }

        }
        record(mockClass, MethodFilter.of(delegateMap::containsKey), (method, args, originReturn) -> {
            Method delegateMethod = delegateMap.get(method);
            try {
                delegateMethod.invoke(null, ArrayUtils.insert(args, 0, originReturn));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new MockException(e.getCause());
            }
        });
    }

    /**
     * 拦截静态方法
     *
     * @see #record(Class, MethodFilter, MethodAssert)
     */
    public static void recordStatic(Class<?> mockClass, MethodFilter methodFilter, MethodAssert methodAssert) {
        record(mockClass, methodFilter.addHead(ModifierUtil::isStatic), methodAssert);
    }

    @SuppressWarnings("java:S106")
    public static void recordMethodCall(Class<?>... mockClasses) {
        recordMethodCall(System.out::println, mockClasses);
    }

    /**
     * @param callLine    方法调用的字符表示
     * @param mockClasses 记录类中所有的非抽象方法调用，不包含 java.和 io.leaderli.litool。类中依赖的其他类的方法调用也会被调用。
     */
    public static void recordMethodCall(Consumer<String> callLine, Class<?>... mockClasses) {
        Set<Class<?>> ref = new HashSet<>();
        for (Class<?> mockClass : mockClasses) {
            if (ref.add(mockClass)) {
                for (String refClass : getCtClass(mockClass).getRefClasses()) {
                    if (PrimitiveEnum.get(refClass) == PrimitiveEnum.OBJECT) {

                        try {
                            ref.add(Class.forName(refClass));
                        } catch (ClassNotFoundException ignored) {
                            // ignore
                        }
                    }
                }
            }
        }
        ref.removeIf(c -> c.getName().startsWith("java") || c.getName().startsWith("io.leaderli.litool"));
        for (Class<?> clazz : ref) {

            record(clazz,
                    MethodFilter.isMethod().add(f -> !ModifierUtil.isAbstract(f)),
                    (method, args, originReturn) -> {
                        String line = StringUtils.getSimpleName(method.getDeclaringClass()) + "." + method.getName() + "(" + StringUtils.join0(",", args, StringUtils::getSimpleName) + ") -> " + StringUtils.getSimpleName(originReturn);
                        callLine.accept(line);
                    });
        }
    }

    /**
     * 断言方法被调用
     *
     * @see Recorder#called()
     * @see Recorder#recordMethodCall
     */
    public static void assertMethodCalled() {

        for (Method method : AbstractRecorder.recordMethodCall) {
            Assertions.assertTrue(AbstractRecorder.actualMethodCall.contains(method), "the method is not called: " + method.getName());
        }
    }

    /**
     * 断言方法未被调用
     *
     * @see Recorder#notCalled()
     * @see Recorder#recordMethodNotCall
     */
    public static void assertMethodNotCalled() {
        for (Method method : AbstractRecorder.recordMethodNotCall) {
            Assertions.assertFalse(AbstractRecorder.actualMethodCall.contains(method), "the method is called: " + method.getName());
        }
    }

    @SuppressWarnings("java:S1751")
    public static void assertDoesNotThrow() {
        for (Throwable throwable : AbstractRecorder.assertThrow) {
            throw new MockException(throwable);
        }
    }

    /**
     * 情况方法调用记录
     *
     * @see Recorder#called()
     */
    public static void clearMethodCalled() {
        AbstractRecorder.actualMethodCall.clear();
    }

    public static void reset() {
        AbstractRecorder.recordMethodCall.clear();
        AbstractRecorder.recordMethodNotCall.clear();
        originClasses.keySet().forEach(LiMock::reset);
    }

    public static void reset(Class<?> clazz) {
        MethodValueFactory.detach(clazz);
    }

    public static Recorder recorder(Class<?> mockClass) {
        return new Recorder(mockClass);
    }

    public static <T> IRecorder<RecordBean<T>, T> recordBean(Class<T> mockClass) {
        return new RecordBean<>(mockClass);
    }

    @SafeVarargs
    public static <T> IRecorder<RecordBeans<T>, T> recordBeans(Class<? extends T>... mockClasses) {
        return new RecordBeans<>(mockClasses);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> T simpleInterface(Class<T> mockClass, Object... returns) {
        MockInterface<T> mockInterface = new MockInterface<>(mockClass);
        for (Method method : mockClass.getMethods()) {
            if (mockInterface.methodValueMap.containsKey(method)) {
                continue;
            }
            for (Object returnValue : returns) {
                if (ClassUtil.isAssignableFromOrIsWrapper(method.getReturnType(), returnValue.getClass())) {
                    MethodValue methodValue = new MethodValue(method);
                    methodValue.other(returnValue);
                    mockInterface.methodValueMap.put(method, methodValue);
                    break;
                }
            }
        }
        return mockInterface.build();
    }


    public static void simple(Class<?> mockClass, Object... returns) {
        if (mockClass.isInterface()) {
            throw new IllegalArgumentException("not support interface " + mockClass);
        }
        Map<Method, Object> returnMap = new HashMap<>();
        for (Method declaredMethod : findDeclaredMethods(mockClass, MethodFilter.isMethod())) {
            if (returnMap.containsKey(declaredMethod)) {
                continue;
            }
            for (Object returnValue : returns) {
                if (ClassUtil.isAssignableFromOrIsWrapper(declaredMethod.getReturnType(), returnValue.getClass())) {
                    returnMap.put(declaredMethod, returnValue);
                    break;
                }
            }
        }
        mock(mockClass, MethodFilter.of(returnMap::containsKey), (m, args) -> returnMap.get(m));
    }

    public static <T> T skipInterface(Class<T> mockClass) {
        return new MockInterface<>(mockClass).build();
    }

    public static void skip(Class<?> mockClass) {
        if (mockClass.isInterface()) {
            throw new IllegalArgumentException("not support interface " + mockClass);
        }
        mock(mockClass, MethodFilter.isMethod(), MethodProxy.NONE);
    }

    public static IMocker<Void, Object> mocker(Class<?> mockClass) {
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
    public static <T, R> MockBean<T, R> mockerBean(Class<T> mockClass) {
        return new MockBean<>(mockClass, true);
    }

    /**
     * 方法记录自动带上实例
     *
     * @param mockClass class
     * @param detach    是否重置之前变动
     */
    public static <T, R> MockBean<T, R> mockerBean(Class<T> mockClass, boolean detach) {
        return new MockBean<>(mockClass, detach);
    }

    /**
     * 方法记录自动带上实例
     */
    @SafeVarargs
    public static <T, R> IMocker<T, R> mockerBeans(Class<? extends T>... mockClass) {
        return new MockBeans<>(mockClass);
    }

    /**
     * 方法记录自动带上实例
     */
    public static <T> MockInterface<T> mockerInterface(Class<T> mockClass) {
        LiAssertUtil.assertTrue(mockClass.isInterface(), "only support interface");
        return new MockInterface<>(mockClass);
    }

    public static <T> MockInterface<T> mockerInterface(LiTypeToken<T> mockType) {
        LiAssertUtil.assertTrue(mockType.getRawType().isInterface(), "only support interface");
        return new MockInterface<>(mockType);
    }


}
