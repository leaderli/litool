package io.leaderli.litool.test;

import java.lang.reflect.Method;

public interface MethodProxy<T> {

    MethodProxy<?> NONE = (m, args) -> null;

    T apply(Method method, Object[] args) throws Throwable;

}
