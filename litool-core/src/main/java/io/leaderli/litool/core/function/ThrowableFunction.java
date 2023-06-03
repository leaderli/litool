package io.leaderli.litool.core.function;

/**
 * 一个声明异常标识的 {@link  java.util.function.Function}
 *
 * @param <T> 函数的输入类型
 * @param <R> 函数的输出类型
 * @author leaderli
 * @since 2022/6/16
 */


@FunctionalInterface

public interface ThrowableFunction<T, R> {

    R apply(T t) throws Throwable;

}
