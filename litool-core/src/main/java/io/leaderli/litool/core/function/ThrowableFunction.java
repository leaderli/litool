package io.leaderli.litool.core.function;

/**
 * * Represents a function that accepts one argument and produces
 * a result which may throw error
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @author leaderli
 * @since 2022/6/16
 */

@FunctionalInterface

public interface ThrowableFunction<T, R> {

    /**
     * Applies this function to the given argument
     *
     * @param t the  function argument
     * @return the function result
     * @throws Throwable error
     */
    R apply(T t) throws Throwable;

}
