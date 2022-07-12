package io.leaderli.litool.core.meta;

/**
 * @author leaderli
 * @since 2022/6/27
 */
public interface RaSubscriber<T> {

    void onSubscribe(RaSubscription subscription);

    /**
     * 发布者推送了一个消息
     *
     * @param t 消息
     */
    void next(Lino<T> t);


    /**
     * 发布者提交一个完成事件
     */
    void onComplete();
}