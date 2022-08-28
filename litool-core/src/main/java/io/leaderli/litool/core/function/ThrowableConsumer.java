package io.leaderli.litool.core.function;

/**
 * Represents an operation that accept a single input argument, throw a throwable and return
 * no result.
 *
 * @param <T> the type of the input to the operation
 * @author leaderli
 * @since 2022/6/16
 */

@FunctionalInterface
public interface ThrowableConsumer<T> {

/**
 * Performs this operation on the given argument.
 *
 * @param t the input argument
 * @throws Throwable error
 */
void accept(T t) throws Throwable;
}
