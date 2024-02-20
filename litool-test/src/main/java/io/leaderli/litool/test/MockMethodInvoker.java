package io.leaderli.litool.test;

import io.leaderli.litool.core.meta.LiTuple;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MockMethodInvoker {


    public static final Map<String, LiTuple<StaticMethodProxy, Method>> invokers = new HashMap<>();

    public static Supplier<?> getInvoke(Class<?> clazz, String invoker, String name, Class<?>[] argsType, Object[] args) {

        LiTuple<StaticMethodProxy, Method> tuple = invokers.get(invoker);
        if (tuple != null) {
            StaticMethodProxy staticMethodProxy = tuple._1;
            if (staticMethodProxy.when(tuple._2, args)) {
                return () -> staticMethodProxy.apply(tuple._2, args);

            }
        }
        return null;
    }

}
