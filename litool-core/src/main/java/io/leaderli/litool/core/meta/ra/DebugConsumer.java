package io.leaderli.litool.core.meta.ra;

/**
 * Represents an operation that when call {@link  Subscriber#next(Object)} or {@link  Subscriber#onNull()}
 *
 * @param <T> the type of the input to the operation
 */
@FunctionalInterface
public interface DebugConsumer<T> {

/**
 * Performs this operation on the given argument of {@link  Subscriber#next(Object)}
 *
 * @param t the input argument
 */
void accept(T t);

/**
 * Perform this operation on {@link  Subscriber#onNull()}
 */
default void onNull() {

}

}
