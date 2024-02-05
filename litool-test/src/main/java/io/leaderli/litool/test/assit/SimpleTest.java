package io.leaderli.litool.test.assit;

import javassist.*;

import java.lang.instrument.ClassDefinition;

public class SimpleTest {


    public static void transfer(Class<?> clazz, Class<?> delegate) {
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass cc = pool.get(clazz.getName());
            CtClass ccd = pool.get(delegate.getName());
            CtConstructor classInitializer = cc.getClassInitializer();
            if (classInitializer != null) {
                CtConstructor classInitializerD = ccd.getClassInitializer();
                if (classInitializerD != null) {
                    classInitializer.setBody(classInitializerD, null);
                } else {
                    classInitializer.setBody("{}");
                }
            }
            for (CtMethod method : cc.getMethods()) {

                try {

                    CtMethod methodD = ccd.getMethod(method.getName(), method.getMethodInfo().getDescriptor());
                    method.setBody(methodD, null);
                } catch (NotFoundException ignore) {
                }
            }
            ClassDefinition definition = new ClassDefinition(clazz, cc.toBytecode());
            RedefineClassAgent.redefineClasses(definition);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
