package io.leaderli.litool.core.meta.ra;

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
    void next(T t);


    /**
     * 发布者提交一个完成事件
     */
    default void onComplete() {

    }

}
