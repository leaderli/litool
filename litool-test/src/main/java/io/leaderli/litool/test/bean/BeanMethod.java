package io.leaderli.litool.test.bean;

import io.leaderli.litool.core.type.ClassUtil;

import java.lang.reflect.Method;
import java.util.function.Supplier;

public class BeanMethod {

    public final Object instance;
    public final Method method;
    public final Supplier<Object> supplier;

    public BeanMethod(Object instance, Method method, Supplier<Object> supplier) {
        this.instance = instance;
        this.method = method;
        this.supplier = supplier;
    }

    @Override
    public String toString() {
        return ClassUtil.shortName(method.getDeclaringClass()) + "#" + method.getName();
    }
}
