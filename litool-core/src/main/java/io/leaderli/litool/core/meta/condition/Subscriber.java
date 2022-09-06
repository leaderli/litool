package io.leaderli.litool.core.meta.condition;

/**
 * 订阅者
 */
@FunctionalInterface
interface Subscriber<T, R> {

    void onSubscribe(Subscription subscription);

    /**
     * 发布者推送了一个消息
     *
     * @param t 源数据
     */
    default void next(T t) {

    }

    default void apply(T t) {

    }

    /**
     * @param value 调用链结束时调用的最终结果
     */
    default void onComplete(R value) {

    }

}
