package io.leaderli.litool.test;

public interface StaticMethodInvoker {

    default boolean can(String methodName, Class<?>[] argsType, Object[] args, Class<?> resultType) {
        return true;
    }

    Object invoke(String name, Class<?>[] argsType, Object[] args, Class<?> resultType);
}
