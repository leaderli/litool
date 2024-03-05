package io.leaderli.litool.test;

import io.leaderli.litool.core.function.Filter;
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
    private final List<LiTuple<Filter<Object[]>, T>> argsFilters = new ArrayList<>();
    private BiFunction<Method, Object[], T> otherFunction;
    private Filter<Object[]> currentArgsFilter;

    MethodValue(Method method) {
        this.method = method;
        this.primitive = PrimitiveEnum.get(method.getReturnType());
    }

    public void args(Object[] currentArgs) {
        this.currentArgsFilter = arg -> Arrays.equals(arg, currentArgs);
    }

    public void argsFilter(Filter<Object[]> argsFilter) {
        this.currentArgsFilter = argsFilter;
    }

    public void then(T result) {
        argsFilters.add(LiTuple.of(currentArgsFilter, result));
    }


    public void args(Object[] compareArgs, T result) {
        argsFilters.add(LiTuple.of(args -> Arrays.equals(args, compareArgs), result));
    }

    public void other(T value) {
        this.otherFunction = (m, args) -> value;
    }

    public void other(BiFunction<Method, Object[], T> otherValue) {
        this.otherFunction = otherValue;
    }

    public Object getMethodValue(Object[] args) {
        for (LiTuple<Filter<Object[]>, T> tuple : argsFilters) {
            if (tuple._1.apply(args)) {
                return primitive.read(tuple._2);
            }
        }

        if (otherFunction != null) {
            return primitive.read(otherFunction.apply(method, args));
        }
        return Either.none();
    }


}
