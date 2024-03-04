package io.leaderli.litool.test;

import java.lang.reflect.Method;

public interface MethodProxy {


    Object apply(Method method, Object[] args) throws Throwable;

}
