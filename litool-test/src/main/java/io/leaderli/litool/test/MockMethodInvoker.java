package io.leaderli.litool.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MockMethodInvoker {


    public static final Map<String, StaticMethodInvoker> invokers = new HashMap<>();

    public static Object getInvoke(String invoker, String name, Class<?>[] argsType, Object[] args, Class<?> resultType) {
        StaticMethodInvoker staticMethodInvoker = invokers.get(invoker);
        if (staticMethodInvoker != null) {
            if (staticMethodInvoker.can(name, argsType, args, resultType)) {
                return staticMethodInvoker;
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
