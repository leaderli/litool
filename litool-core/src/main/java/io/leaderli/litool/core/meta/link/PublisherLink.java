package io.leaderli.litool.core.meta.link;

/**
 * @author leaderli
 * @since 2022/6/27
 */
@FunctionalInterface
public interface PublisherLink<T> {


    void subscribe(SubscriberLink<T> subscriber);
}
