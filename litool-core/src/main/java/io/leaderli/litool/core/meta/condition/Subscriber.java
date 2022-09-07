package io.leaderli.litool.core.meta.condition;

/**
 * Will receive call to {@link  #onSubscribe(Subscription)} once after passing an  instance
 * of  {@link  Subscriber} to {@link  Publisher#subscribe(Subscriber)}
 * <p>
 * Demand can be signaled via {@link  Subscription#request()}, the signaled should stop at
 * the {@link  Subscriber} which call {@link #onComplete(Object)}
 *
 * @param <T> the type of source value
 * @param <R> the type of target value
 */
@FunctionalInterface
interface Subscriber<T, R> {

    void onSubscribe(Subscription subscription);

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
