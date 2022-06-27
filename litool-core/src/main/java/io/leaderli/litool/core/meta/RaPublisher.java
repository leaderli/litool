package io.leaderli.litool.core.meta;

/**
 * @author leaderli
 * @since 2022/6/27
 */
public interface RaPublisher<T> {


    void subscribe(RaSubscriber<? super T> subscriber);
}
