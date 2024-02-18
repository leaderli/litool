package io.leaderli.litool.test;

import io.leaderli.litool.core.text.StrSubstitution;
import io.leaderli.litool.core.text.StringUtils;
import javassist.*;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.asm.Advice;

import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class LiMock {
    public static final Map<Class<?>, byte[]> originClasses = new HashMap<>();
    public static final ByteBuddy byteBuddy = new ByteBuddy();
    static Instrumentation instrumentation = ByteBuddyAgent.install();



    private static void skipClassInitializer(CtClass ct) throws CannotCompileException {
        CtConstructor classInitializer = ct.makeClassInitializer();
        classInitializer.setBody("{}");
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
        System.out.println(bytecode.length);
        originClasses.putIfAbsent(clazz, bytecode);
        System.out.println(originClasses.get(clazz).length);
        return ct;
    }

    private static void mockStaticMethod(CtClass ct, StaticMethodInvoker invoker) throws CannotCompileException {

        ct.getClassPool().importPackage("io.leaderli.litool.test.MockMethodInvoker");
        ct.getClassPool().importPackage("io.leaderli.litool.test.StaticMethodInvoker");
        for (CtMethod method : ct.getDeclaredMethods()) {
            if (method.getDeclaringClass() == ct && Modifier.isStatic(method.getModifiers())) {
                String invokerClassName = invoker.getClass().getName();
                MockMethodInvoker.invokers.put(invokerClassName, invoker);

                String src = StrSubstitution.format2("java.util.function.Supplier invoker = MockMethodInvoker.getInvoke($class,#id#,#name#,$sig,$args);\r\n" + "if( invoker!=null) return ($r)invoker.get();", "#", "#", StringUtils.wrap(invokerClassName, '"'), StringUtils.wrap(method.getName(), '"'));
                method.insertBefore(src);
            }
        }


    }

    public static void mockStatic(Class<?> mockClass, StaticMethodInvoker invoker) {
        mockStatic(mockClass, invoker, false);
    }

    public static void mockStatic(Class<?> mockClass, StaticMethodInvoker invoker, boolean ignoreStaticBlock) {
        try {

            CtClass ct = backupOrRestore(mockClass);
            if (ignoreStaticBlock) {
                skipClassInitializer(ct);
            }
            mockStaticMethod(ct, invoker);
            byte[] bytecode = ct.toBytecode();
            ct.defrost();
            System.out.println("redefine " + bytecode.length);
            instrumentation.redefineClasses(new ClassDefinition(mockClass, bytecode));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object invoke() {

        return 4;
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
