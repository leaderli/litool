package io.leaderli.litool.core.meta.ra;

/**
 * Will receive call to {@link #onSubscribe(SubscriptionRa)} once after passing an instance of
 * {@link SubscriberRa} to {@link PublisherRa#subscribe(SubscriberRa)}
 * <p>
 * No further notifications will be received until {@link SubscriptionRa#request(int)} is called.
 * <p>
 * After signaling demand:
 * <p>
 * Demand can be signaled via {@link SubscriptionRa#request(int)} whenever the {@link SubscriberRa} instance is
 * capable of
 * handling more.
 *
 * @param <T> the type of element signaled.
 * @author leaderli
 * @since 2022/6/27
 */
public interface SubscriberRa<T> extends Exceptionable, Completable, Cancelable {

    /**
     * Invoked after calling {@link PublisherRa#subscribe(SubscriberRa)}
     * <p>
     * The {@link PublisherRa} will send notifications only to response {@link SubscriptionRa#request(int)}
     *
     * @param subscription {@link SubscriptionRa} that allows requesting data via {@link SubscriptionRa#request(int)}
     */
    void onSubscribe(SubscriptionRa subscription);

    /**
     * Data notification sent by the {@link PublisherRa} in response to request
     * to {@link SubscriptionRa#request(int)} when the element is not null
     *
     * @param t the element signaled
     */
    void next(T t);

    /**
     * Data notification sent by the {@link PublisherRa} in response to request
     * to {@link SubscriptionRa#request(int)} when the element is null
     */
    default void next_null() {

    }


}
