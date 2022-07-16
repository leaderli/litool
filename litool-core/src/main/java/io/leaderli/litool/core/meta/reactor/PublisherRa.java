package io.leaderli.litool.core.meta.reactor;

/**
 * @author leaderli
 * @since 2022/6/27
 */
public interface PublisherRa<T> {


    void subscribe(SubscriberRa<? super T> subscriber);
}
