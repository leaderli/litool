package io.leaderli.litool.test;

import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.type.PrimitiveEnum;
import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MockMethodInvoker {


    public static final Map<String, LiTuple<MethodProxy, Method>> invokers = new HashMap<>();
    public static final Map<String, MethodAssert> recorders = new HashMap<>();

    public static Supplier<?> getInvoke(String uuid, Class<?> clazz, String name, Class<?>[] argsType, Object[] args) {

        LiTuple<MethodProxy, Method> tuple = invokers.get(uuid);
        if (tuple != null) {
            MethodProxy methodProxy = tuple._1;
            if (methodProxy.when(tuple._2, args)) {
                return () -> methodProxy.apply(tuple._2, args);

            }
        }
        return null;
    }


    public static void record(String uuid, Object _this, Object[] args, Object _return) {
        String apply = recorders.get(uuid).apply(_this, args, _return);
        if (apply != null) {
            Assertions.fail(Arrays.toString(args) + " -> " + _return + "\r\n" + apply);
        }
    }

    public static Object zero(Class<?> returnClass) {
        return PrimitiveEnum.get(returnClass).zero_value;
    }

}
