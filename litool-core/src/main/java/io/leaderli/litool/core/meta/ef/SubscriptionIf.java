package io.leaderli.litool.core.meta.ef;

/**
 * A {@link  SubscriptionIf} represent a one-to-one lifecycle of a {@link  SubscriberIf} subscribing
 * to a {@link  PublisherIf}
 * <p>
 * It can only be used once by a single {@link  SubscriberIf}
 */
@FunctionalInterface
public interface SubscriptionIf {

    /**
     * No events will be send by a {@link  PublisherIf} util signaled via this method
     */
    void request();


}
