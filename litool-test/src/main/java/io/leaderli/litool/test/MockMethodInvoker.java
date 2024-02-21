package io.leaderli.litool.test;

import io.leaderli.litool.core.meta.LiTuple;
import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class MockMethodInvoker {


    public static final Map<String, LiTuple<StaticMethodProxy, Method>> invokers = new HashMap<>();
    public static final Map<String, BiFunction<Object[], Object, String>> recorders = new HashMap<>();

    public static Supplier<?> getInvoke(String uuid, Class<?> clazz, String name, Class<?>[] argsType, Object[] args) {

        LiTuple<StaticMethodProxy, Method> tuple = invokers.get(uuid);
        if (tuple != null) {
            StaticMethodProxy staticMethodProxy = tuple._1;
            if (staticMethodProxy.when(tuple._2, args)) {
                return () -> staticMethodProxy.apply(tuple._2, args);

            }
        }
        return null;
    }


    public static void record(String uuid, Object[] args, Object returnValue) {
        String apply = recorders.get(uuid).apply(args, returnValue);
        if (apply != null) {
            Assertions.fail(Arrays.toString(args) + " -> " + returnValue + "\r\n" + apply);
        }
    }

}
