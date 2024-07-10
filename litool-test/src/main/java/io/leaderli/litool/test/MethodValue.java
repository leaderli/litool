package io.leaderli.litool.test;

import io.leaderli.litool.core.function.Filter;
import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.type.PrimitiveEnum;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MethodValue<T> {
    private final Method method;
    private final PrimitiveEnum primitive;
    private final List<LiTuple<Filter<Object[]>, T>> argsFilters = new ArrayList<>();
    private MethodProxy<T> otherFunction;
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
        argsFilters.add(0, LiTuple.of(currentArgsFilter, result));
    }


    public void args(Object[] compareArgs, T result) {
        argsFilters.add(0, LiTuple.of(args -> Arrays.equals(args, compareArgs), result));
    }

    public void other(T value) {
        this.otherFunction = (m, args) -> value;
    }

    public void otherFunction(MethodProxy<T> otherFunction) {
        this.otherFunction = otherFunction;
    }

    public Object getMethodValue(Object[] args) {
        for (LiTuple<Filter<Object[]>, T> tuple : argsFilters) {
            if (tuple._1.apply(args)) {
                return primitive.read(tuple._2);
            }
        }

        if (otherFunction != null) {
            try {
                return primitive.read(otherFunction.apply(method, args));
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return LiMock.SKIP_MARK;
    }


}
