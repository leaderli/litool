package io.leaderli.litool.core.meta.ra;

/**
 * A {@link SubscriptionRa} represents a one-to-one lifecycle of a {@link SubscriberRa} subscribing
 * to a {@link PublisherRa}
 * <p>
 * It can only be used once by a single {@link SubscriberRa}
 * <p>
 * It is used to both signal desire for data and cancel demand
 *
 * @author leaderli
 * @since 2022/6/27
 */
public interface SubscriptionRa extends CancelSubscription {

    /**
     * 请求数据
     */
    void request();


}
