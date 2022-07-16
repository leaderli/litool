package io.leaderli.litool.core.meta.reactor;

import io.leaderli.litool.core.meta.Lino;

/**
 * @author leaderli
 * @since 2022/6/27
 */
public interface SubscriberRa<T> {

    void onSubscribe(SubscriptionRa subscription);

    /**
     * 发布者推送了一个消息
     *
     * @param t 消息
     */
    void next(Lino<? extends T> t);


    /**
     * 发布者提交一个完成事件
     */
    default void onComplete() {

    }

}
