package io.leaderli.litool.test;

import java.lang.reflect.Method;

public interface StaticMethodInvoker {

    default boolean can(Method method, Object[] args) {
        return true;
    }

    Object invoke(Method method, Object[] args);
}
