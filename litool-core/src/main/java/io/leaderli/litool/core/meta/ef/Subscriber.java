package io.leaderli.litool.core.meta.ef;

/**
 * 用于观察者模式中的订阅者，用于接收发布者发送的数据流并进行处理。
 *
 * @param <T> 数据流中的元素类型
 * @param <R> 处理后的结果类型
 */
@FunctionalInterface
public interface Subscriber<T, R> {

    /**
     * 当订阅成功后，会调用该方法，传入 SubscriptionIf 实例，表示可以开始接收数据流。
     *
     * @param subscription 订阅实例
     */
    void onSubscribe(Subscription subscription);

    /**
     * 当数据流中的元素不满足条件时，会调用该方法，默认实现为空。
     *
     * @param t 数据流中的元素
     */
    default void next(T t) {

    }

    /**
     * 当数据流中的元素满足条件时，会调用该方法，默认实现为空。
     *
     * @param t 数据流中的元素
     */
    default void apply(T t) {

    }

    /**
     * 当数据流处理完毕时，会调用该方法，传入处理后的结果， 默认实现为空。
     *
     * @param value 处理后的结果
     */
    default void onComplete(R value) {

    }

}
