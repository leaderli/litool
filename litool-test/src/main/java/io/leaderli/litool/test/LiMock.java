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
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class LiMock {
    public static final Map<Class<?>, byte[]> originClasses = new HashMap<>();
    public static final ByteBuddy byteBuddy = new ByteBuddy();
    static Instrumentation instrumentation = ByteBuddyAgent.install();

    private static void skipClassInitializer(Class<?> mockingClass) {


        try {

            ClassPool cl = ClassPool.getDefault();
            CtClass ct = cl.getCtClass(mockingClass.getName());
            byte[] bytecode = ct.toBytecode();
            ct.defrost();
            originClasses.putIfAbsent(mockingClass, bytecode);
            skipClassInitializer(ct);
            bytecode = ct.toBytecode();
            ct.defrost();
            instrumentation.redefineClasses(new ClassDefinition(mockingClass, bytecode));
//            Class.forName(mockingClass.getName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void skipClassInitializer(CtClass ct) throws CannotCompileException {
        CtConstructor classInitializer = ct.makeClassInitializer();
        classInitializer.setBody("{}");
    }

    private static CtClass backup(Class<?> clazz) throws NotFoundException, IOException, CannotCompileException {
        ClassPool cl = ClassPool.getDefault();
        CtClass ct = cl.getCtClass(clazz.getName());
        byte[] bytecode = ct.toBytecode();
        ct.defrost();
        originClasses.putIfAbsent(clazz, bytecode);
        return ct;
    }

    private static void mockStaticMethod(CtClass ct, StaticMethodInvoker invoker) throws CannotCompileException, InstantiationException, IllegalAccessException, IOException {

        ct.getClassPool().importPackage("io.leaderli.litool.test.MockMethodInvoker");
        ct.getClassPool().importPackage("io.leaderli.litool.test.StaticMethodInvoker");
        for (CtMethod method : ct.getMethods()) {
            if (method.getDeclaringClass() == ct && Modifier.isStatic(method.getModifiers())) {
                String invokerClassName = invoker.getClass().getName();
                MockMethodInvoker.invokers.put(invokerClassName, invoker);

                String src = StrSubstitution.format2("StaticMethodInvoker invoker = MockMethodInvoker.getInvoke(#id#,#name#,$sig,$args,$type);\r\n" + "if( invoker!=null) return ($r)invoker.invoke(#name#,$sig,$args,$type);", "#", "#", StringUtils.wrap(invokerClassName, '"'), StringUtils.wrap(method.getName(), '"'));
                method.insertBefore(src);
            }
        }


    }

    public static void mockStatic(Class<?> mockClass, StaticMethodInvoker invoker, boolean ignoreStaticBlock) {
        ClassPool cl = ClassPool.getDefault();
        try {

            CtClass ct = backup(mockClass);
            if (ignoreStaticBlock) {
                skipClassInitializer(ct);
            }
            mockStaticMethod(ct, invoker);
            byte[] bytecode = ct.toBytecode();
            ct.defrost();
            instrumentation.redefineClasses(new ClassDefinition(mockClass, bytecode));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object invoke() {

        return 4;
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
