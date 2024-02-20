package io.leaderli.litool.test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MockMethodInvoker {


    public static final Map<String, StaticMethodProxy> invokers = new HashMap<>();

    public static Supplier<?> getInvoke(Class<?> clazz, String invoker, String name, Class<?>[] argsType, Object[] args) {
        StaticMethodProxy staticMethodProxy = invokers.get(invoker);
        if (staticMethodProxy != null) {
            try {
                Method declaredMethod = clazz.getDeclaredMethod(name, argsType);
                if (staticMethodProxy.when(declaredMethod, args)) {
                    return () -> staticMethodProxy.apply(declaredMethod, args);
                }
            } catch (NoSuchMethodException ignore) {
            }
        }
        return null;
    }

}
