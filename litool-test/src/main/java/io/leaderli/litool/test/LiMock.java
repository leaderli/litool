package io.leaderli.litool.test;

import io.leaderli.litool.core.collection.ArrayUtils;
import io.leaderli.litool.core.text.StrSubstitution;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.core.type.MethodUtil;
import io.leaderli.litool.core.type.ModifierUtil;
import javassist.*;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.asm.Advice;

import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class LiMock {
    public static final Map<Class<?>, byte[]> originClasses = new HashMap<>();
    public static final ByteBuddy byteBuddy = new ByteBuddy();
    public static Instrumentation instrumentation = ByteBuddyAgent.install();

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


    private static CtClass backupOrRestore(Class<?> clazz) throws NotFoundException, IOException, CannotCompileException {
        ClassPool cl = ClassPool.getDefault();

        CtClass ct = cl.getCtClass(clazz.getName());
        if (originClasses.containsKey(clazz)) {
            byte[] bytes = originClasses.get(clazz);
            try {
                instrumentation.redefineClasses(new ClassDefinition(clazz, bytes));
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


    /**
     * 根据筛选器来代理满足条件的静态方法,将方法由 {@link  StaticMethodProxy} 去执行。
     * 可以通过{@link  StaticMethodProxy#when(Method, Object[])}来根据参数来决定是否需要最终由
     * {@link  StaticMethodProxy#apply(Method, Object[])}代理执行
     *
     * @param mockClass         代理类
     * @param mockMethodFilter  代理方法的过滤类，用于筛选需要代理的方法
     * @param staticMethodProxy 代理函数
     */
    public static void mockStatic(Class<?> mockClass, Function<Method, Boolean> mockMethodFilter, StaticMethodProxy staticMethodProxy) {
        try {

            CtClass ct = backupOrRestore(mockClass);
            ClassPool classPool = ct.getClassPool();
            classPool.importPackage("io.leaderli.litool.test.MockMethodInvoker");
            classPool.importPackage("io.leaderli.litool.test.StaticMethodInvoker");
            for (Method method : mockClass.getDeclaredMethods()) {

                CtClass[] params = ArrayUtils.map(method.getParameterTypes(), CtClass.class, c -> {
                    try {
                        return classPool.getCtClass(c.getName());
                    } catch (NotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });
                if (method.getDeclaringClass() == mockClass && ModifierUtil.isStatic(method) && mockMethodFilter.apply(method)) {
                    CtMethod ctMethod = ct.getDeclaredMethod(method.getName(), params);
                    String invokerClassName = staticMethodProxy.getClass().getName();
                    MockMethodInvoker.invokers.put(invokerClassName, staticMethodProxy);
                    String src = StrSubstitution.format2("java.util.function.Supplier staticMethodProxy = MockMethodInvoker.getInvoke($class,#id#,#name#,$sig,$args);\r\n"
                                    + "if( staticMethodProxy!=null) return ($r)staticMethodProxy.get();",
                            "#", "#",
                            StringUtils.wrap(invokerClassName, '"'), StringUtils.wrap(method.getName(), '"')
                    );
                    ctMethod.insertBefore(src);
                }
            }

            redefineClasses(mockClass, ct);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
}

class MockAdvice {
    @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
    public static Object enter(@Advice.Origin Method method, @Advice.AllArguments Object[] args) throws Throwable {
        System.out.println("bytebuddy method call before  ");
        Object result = method.invoke(null, args);
        System.out.println("bytebuddy method call after   ");
        return result;
    }
}
