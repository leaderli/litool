package io.leaderli.litool.core.meta.ra;

/**
 * Represents an operation that when call {@link  SubscriberRa#next(Object)} or {@link  SubscriberRa#next_null()}
 *
 * @param <T> the type of the input to the operation
 */
@FunctionalInterface
public interface DebugConsumer<T> {

    /**
     * Performs this operation on the given argument of {@link  SubscriberRa#next(Object)}
     *
     * @param t the input argument
     */
    void accept(T t);

    /**
     * Perform this operation on {@link  SubscriberRa#next_null()}
     */
    default void onNull() {

        System.out.println("onNull");
    }

}
