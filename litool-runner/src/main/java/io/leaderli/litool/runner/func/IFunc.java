package io.leaderli.litool.runner.func;

import io.leaderli.litool.core.exception.LiAssertUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class IFunc {

    final Method method;


    protected IFunc(Method method) {
        this.method = method;
        LiAssertUtil.assertTrue(this.method.isAccessible());
    }

    public final String apply(Object[] params) throws InvocationTargetException, IllegalAccessException {
        return (String) method.invoke(this, params);
    }

    public final Class<?>[] support() {
        return method.getParameterTypes();
    }
}
