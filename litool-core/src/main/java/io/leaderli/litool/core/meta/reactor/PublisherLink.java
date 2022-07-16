package io.leaderli.litool.core.meta.reactor;

/**
 * @author leaderli
 * @since 2022/6/27
 */
public interface PublisherLink<T> {


    void subscribe(SubscriberLink<T> subscriber);
}
