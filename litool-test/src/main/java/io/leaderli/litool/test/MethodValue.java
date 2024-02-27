package io.leaderli.litool.test;

import io.leaderli.litool.core.collection.ArrayEqual;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class MethodValue<T, R> {
    final Method method;
    final Map<ArrayEqual<T>, R> whenValue = new HashMap<>();
    BiFunction<Method, Object[], R> otherValue;

    MethodValue(Method method) {
        this.method = method;
    }
}
