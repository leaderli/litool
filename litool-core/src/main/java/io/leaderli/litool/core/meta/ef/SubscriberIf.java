package io.leaderli.litool.core.meta.ef;

/**
 * Will receive call to {@link  #onSubscribe(SubscriptionIf)} once after passing an  instance
 * of  {@link  SubscriberIf} to {@link  PublisherIf#subscribe(SubscriberIf)}
 * <p>
 * Demand can be signaled via {@link  SubscriptionIf#request()}, the signaled should stop at
 * the {@link  SubscriberIf} which call {@link #onComplete(Object)}
 *
 * @param <T> the type of source value
 * @param <R> the type of target value
 */
@FunctionalInterface
public interface SubscriberIf<T, R> {

    void onSubscribe(SubscriptionIf subscription);

    /**
     * called on not match the predicate
     *
     * @param t the source value
     */
    default void next(T t) {

    }

    /**
     * called on match the predicate
     *
     * @param t the source value
     */

    default void apply(T t) {

    }

    /**
     * called on chain  finished
     *
     * @param value the target value
     */
    default void onComplete(R value) {

    }

}
