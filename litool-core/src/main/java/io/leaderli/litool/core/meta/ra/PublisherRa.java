package io.leaderli.litool.core.meta.ra;

/**
 * A {@link PublisherRa} is a provider of a potentially unbounded number of sequenced elements, publishing them
 * according to the demand received from its {@link SubscriberRa}
 * <p>
 * A {@link PublisherRa} can serve multiple {@link SubscriberRa} subscribed {@link #subscribe(SubscriberRa)} dynamically
 * at various points in time
 *
 * @param <T> the type of element signaled
 * @author leaderli
 * @since 2022/6/27
 */
@FunctionalInterface
public interface PublisherRa<T> {


    /**
     * Request  to start streaming data
     * <p>
     * This is a "factory method" and can be call multi times, each time starting a new  {@link  SubscriptionRa}
     * <p>
     * Each {@link SubscriptionRa} will work for only a single {@link SubscriberRa}
     *
     * @param subscriber the  {@link SubscriberRa} will consume signal from this
     */
    void subscribe(SubscriberRa<? super T> subscriber);


}
