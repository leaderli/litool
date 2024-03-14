package io.leaderli.litool.test;

import java.lang.reflect.Method;

public interface MethodProxy<T> {


    T apply(Method method, Object[] args) throws Throwable;

}
