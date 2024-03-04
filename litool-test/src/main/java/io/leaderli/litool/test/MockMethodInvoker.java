package io.leaderli.litool.test;

import io.leaderli.litool.core.meta.Either;
import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.type.PrimitiveEnum;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MockMethodInvoker {


    public static final Map<String, LiTuple<MethodProxy, Method>> invokers = new HashMap<>();
    public static final Map<String, LiTuple<MethodAssert, Method>> recorders = new HashMap<>();

    public static Either<?, ?> invoke(String uuid, Class<?> clazz, String name, Class<?>[] argsType, Object[] args) {

        LiTuple<MethodProxy, Method> tuple = invokers.get(uuid);
        if (tuple == null) {
            return Either.none();
        }
        Object apply = tuple._1.apply(tuple._2, args);
        if (apply instanceof Either) {
            return (Either<?, ?>) apply;
        }
        return Either.right(apply);

    }


    public static void record(String uuid, Object _this, Object[] args, Object _return) {
        LiTuple<MethodAssert, Method> tuple = recorders.get(uuid);
        if (tuple == null) {
            return;
        }
        try {
            tuple._1.apply(tuple._2, _this, args, _return);
        } catch (Throwable throwable) {
            Recorder.assertThrow.add(throwable);
            throw throwable;
        }
    }

    public static Object zero(Class<?> returnClass) {
        return PrimitiveEnum.get(returnClass).zero_value;
    }

}
