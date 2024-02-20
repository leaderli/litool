package io.leaderli.litool.test;

import java.lang.reflect.Method;

public interface StaticMethodProxy {

    default boolean when(Method method, Object[] args) {
        return true;
    }

    Object apply(Method method, Object[] args);
}
