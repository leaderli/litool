package io.leaderli.litool.core.type;

import io.leaderli.litool.core.exception.ExceptionUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UnwrapInvocationHandler implements InvocationHandler {

    private final InvocationHandler invocationHandler;

    public UnwrapInvocationHandler(InvocationHandler invocationHandler) {
        this.invocationHandler = invocationHandler;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            return invocationHandler.invoke(proxy, method, args);
        } catch (Throwable throwable) {
            throw ExceptionUtil.unwrapThrowable(throwable);
        }
    }
}
