package io.leaderli.litool.test;

import io.leaderli.litool.core.function.Filter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class MethodValue<T> {
    final Method method;
    final Map<Filter<Object[]>, T> whenValue = new HashMap<>();
    BiFunction<Method, Object[], T> otherValue;

    MethodValue(Method method) {
        this.method = method;
    }
}
