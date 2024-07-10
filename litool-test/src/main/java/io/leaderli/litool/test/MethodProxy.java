package io.leaderli.litool.test;

import java.lang.reflect.Method;

public interface MethodProxy<T> {

    MethodProxy<?> NONE = (m, args) -> null;

    static <T> MethodProxy<T> of(T value) {
        return (m, args) -> value;
    }

    T apply(Method method, Object[] args) throws Throwable;

}
