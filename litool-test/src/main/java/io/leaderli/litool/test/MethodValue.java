package io.leaderli.litool.test;

import io.leaderli.litool.core.meta.Either;
import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.type.PrimitiveEnum;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

public class MethodValue<T> {
    private final Method method;
    private final PrimitiveEnum primitive;
    private final List<LiTuple<Object[], T>> argsFunction = new ArrayList<>();
    private BiFunction<Method, Object[], T> otherFunction;
    private Object[] currentArgs;

    MethodValue(Method method) {
        this.method = method;
        this.primitive = PrimitiveEnum.get(method.getReturnType());
    }

    public void args(Object[] currentArgs) {
        this.currentArgs = currentArgs;
    }


    public void then(T result) {
        argsFunction.add(LiTuple.of(currentArgs, result));
    }


    public void args(Object[] args, T result) {
        argsFunction.add(LiTuple.of(args, result));
    }

    public void other(T value) {
        this.otherFunction = (m, args) -> value;
    }

    public void other(BiFunction<Method, Object[], T> otherValue) {
        this.otherFunction = otherValue;
    }

    public Object getMethodValue(Object[] args) {
        for (LiTuple<Object[], T> tuple : argsFunction) {
            if (Arrays.equals(tuple._1, args)) {
                return primitive.read(tuple._2);
            }
        }

        if (otherFunction != null) {
            return primitive.read(otherFunction.apply(method, args));
        }
        return Either.none();
    }


}
