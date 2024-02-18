package io.leaderli.litool.test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MockMethodInvoker {


    public static final Map<String, StaticMethodInvoker> invokers = new HashMap<>();


    public static Supplier<?> getInvoke(Class<?> clazz, String invoker, String name, Class<?>[] argsType, Object[] args) {
        StaticMethodInvoker staticMethodInvoker = invokers.get(invoker);
        if (staticMethodInvoker != null) {
            try {
                Method declaredMethod = clazz.getDeclaredMethod(name, argsType);
                if (staticMethodInvoker.can(declaredMethod, args)) {
                    return () -> staticMethodInvoker.invoke(declaredMethod, args);
                }
            } catch (NoSuchMethodException ignore) {
            }
        }
        return null;
    }

    public static Object invoke(String name, Class<?>[] argsType, Object[] args, Class<?> resultType) {
        System.out.println(name);
        System.out.println(Arrays.toString(argsType));
        System.out.println(Arrays.toString(args));
        System.out.println(resultType);
        return 3;
    }
}
