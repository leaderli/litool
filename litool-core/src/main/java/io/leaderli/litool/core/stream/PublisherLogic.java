package io.leaderli.litool.core.stream;

/**
 * @author leaderli
 * @since 2022/9/12
 */
@FunctionalInterface
public interface PublisherLogic<T> {

    void subscribe(SubscriberLogic<T> subscriber);

}
