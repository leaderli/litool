package io.leaderli.litool.core.meta.reactor;

import io.leaderli.litool.core.meta.Lino;

/**
 * @author leaderli
 * @since 2022/6/27
 */
public interface SubscriberLink<T> {

    void onSubscribe(SubscriptionLink<T> subscription);

    /**
     * 发布者推送了一个消息
     *
     * @param lino 消息
     */
    void next(T lino);


    /**
     * 执行链条中断时调用
     *
     * @param lino 实际值
     */
    default void onCancel(Lino<T> lino) {

    }

}
