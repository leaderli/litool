package io.leaderli.litool.core.function;

/**
 * @author leaderli
 * @since 2022/6/16
 */
@FunctionalInterface

public interface ThrowableFunction<T, R> {


    R apply(T t) throws Throwable;

}
