package io.leaderli.litool.test;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.type.PrimitiveEnum;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;

public class LiMockCartesian {


    public static final Map<Method, Object[]> cache = new HashMap<>();
    public static final Set<Class<?>> mockClass = new HashSet<>();
    private static Method mockMethod;
    private static boolean mockProgress;

    public static void mock(Class<?> cls) {

        ByteBuddyAgent.install();

        new ByteBuddy()
                .redefine(cls)
                .visit(Advice.to(MockMethodAdvice.class).on(target -> target.isMethod() && !target.isConstructor()))
                .make()
                .load(cls.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

    }

    @SafeVarargs
    public static <T> void when(Supplier<T> runnable, T... result) {

        mockMethod = null;
        mockProgress = true;
        runnable.get();

        Objects.requireNonNull(mockMethod);
        LiAssertUtil.assertTrue(ClassUtil.isAssignableFromOrIsWrapper(mockMethod.getReturnType(), result.getClass().getComponentType()));
        cache.put(mockMethod, result);
        mockClass.add(mockMethod.getDeclaringClass());
        mockMethod = null;
        mockProgress = false;
//        cache.forEach((k, v) -> {
//
//            ConsoleUtil.print(k.getName(), Arrays.toString(v));
//
//        });
    }

    public static class MockMethodAdvice {


        @SuppressWarnings("UnusedAssignment")
        @Advice.OnMethodExit
        public static void enter(
                @Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object ret,
                @Advice.Origin Method origin,
                @Advice.AllArguments Object[] arguments) {


            Object[] objects = cache.get(origin);
            Object ret1 = null;
            if (objects != null && objects.length > 0) {
                ret1 = objects[0];

            }
//                    Lino.of(cache.get(origin)).map(a -> a[0]).get();
            if (ret1 == null && origin.getReturnType().isPrimitive()) {
                ret1 = PrimitiveEnum.get(origin.getReturnType()).zero_value;
            }

            if (mockProgress) {
                mockMethod = origin;
            }
//            print(origin, ret, ret1);
            ret = ret1;
//            ret = 2;
//            ret = origin.getName();
        }
    }
}
