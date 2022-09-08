package io.leaderli.litool.core.meta.ef;

/**
 * A {@link  PublisherIf} can publishing source value according to the demand from its {@link  SubscriberIf}
 *
 * @param <T> the type of source value
 * @param <R> the type of target value
 */
@FunctionalInterface
public interface PublisherIf<T, R> {
    /**
     * Request {@link  PublisherIf} to start the chain
     * <p>
     * This is a "factory method" and can be call multi times, each time starting a new {@link  SubscriptionIf}
     * <p>
     * Each {@link  SubscriptionIf} will work only for a single {@link SubscriberIf}
     *
     * @param subscriber the subscriber will consume signal from {@link PublisherIf}
     * @see SubscriberIf#onSubscribe(SubscriptionIf)
     */
    void subscribe(SubscriberIf<? super T, R> subscriber);
}
