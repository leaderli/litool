package io.leaderli.litool.test;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.type.ClassUtil;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;

public class LiMockCartesian {


    public static final Map<Method, Object[]> methodValues = new HashMap<>();
    public static final Set<Class<?>> mockClass = new HashSet<>();
    public static Method mockMethod;
    public static boolean mockProgress;

    public static void clear() {
        mockClass.clear();
        methodValues.clear();
        mockMethod = null;
        mockProgress = false;
    }

    @SuppressWarnings("resource")
    public static void mock(Class<?> cls) {

        ByteBuddyAgent.install();

        new ByteBuddy()
                .redefine(cls)
                .visit(Advice.to(MockMethodAdvice.class).on(target -> target.isMethod() && !target.isConstructor()))
                .make()
                .load(cls.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

    }

    @SafeVarargs
    public static <T> void when(Supplier<T> runnable, T... mockValues) {


        if (mockValues.length == 0) {
            return;
        }

        mockMethod = null;
        mockProgress = true;
        runnable.get();

        Objects.requireNonNull(mockMethod);
        LiAssertUtil.assertTrue(ClassUtil.isAssignableFromOrIsWrapper(mockMethod.getReturnType(), mockValues.getClass().getComponentType()));
        methodValues.put(mockMethod, mockValues);
        mockClass.add(mockMethod.getDeclaringClass());

        mockMethod = null;
        mockProgress = false;
    }

    public static class MockMethodAdvice {
        @SuppressWarnings("all")
        @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
        public static Object enter(@Advice.Origin Method origin) {
            if (mockProgress) {
                mockMethod = origin;
            }
            return 0;
        }
    }
}
