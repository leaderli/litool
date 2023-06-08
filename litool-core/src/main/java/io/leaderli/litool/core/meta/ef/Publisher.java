package io.leaderli.litool.core.meta.ef;

/**
 * PublisherIf是一个发布者接口，可以根据订阅者的需求发布源值。
 *
 * @param <T> 源值的类型
 * @param <R> 目标值的类型
 */
@FunctionalInterface
public interface Publisher<T, R> {
    /**
     * 请求PublisherIf启动链。
     * <p>
     * 这是一个“工厂方法”，可以多次调用，每次启动一个新的SubscriptionIf。
     * <p>
     * 每个SubscriptionIf仅适用于单个SubscriberIf。
     *
     * @param subscriber 将从PublisherIf消耗信号的订阅者
     * @see Subscriber#onSubscribe(Subscription)
     */
    void subscribe(Subscriber<? super T, R> subscriber);
}
