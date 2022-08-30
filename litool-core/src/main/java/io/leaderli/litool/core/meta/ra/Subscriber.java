package io.leaderli.litool.core.meta.ra;

/**
 * Will receive call to {@link #onSubscribe(Subscription)} once after passing an instance of {@link Subscriber} to
 * {@link Publisher#subscribe(Subscriber)}
 * <p>
 * No further notifications will be received until {@link Subscription#request()} is called.
 * <p>
 * After signaling demand:
 * <p>
 * Demand can be signaled via {@link Subscription#request()} whenever the {@link Subscriber} instance is capable of
 * handling more.
 *
 * @param <T> the type of element signaled.
 * @author leaderli
 * @since 2022/6/27
 */
public interface Subscriber<T> {

    /**
     * Invoked after calling {@link Publisher#subscribe(Subscriber)}
     * <p>
     * The {@link Publisher} will send notifications only to response {@link Subscription#request()}
     *
     * @param subscription {@link Subscription} that allows requesting data via {@link Subscription#request()}
     */
    void onSubscribe(Subscription subscription);

    /**
     * Data notification sent by the {@link Publisher} in response to request
     * to {@link Subscription#request()} when the element is not null
     *
     * @param t the element signaled
     */
    void next(T t);

    /**
     * Data notification sent by the {@link Publisher} in response to request
     * to {@link Subscription#request()} when the element is null
     */
    default void onNull() {

    }


    /**
     * all element have performed
     */
    default void onComplete() {

    }

    /**
     * invoked when response to notification has throw error, can use {@link CancelSubscription#cancel()} to cancel the
     * demand
     *
     * @param t      the element signaled
     * @param cancel use to cancel demand
     */
    default void onError(Throwable t, CancelSubscription cancel) {
    }

}
