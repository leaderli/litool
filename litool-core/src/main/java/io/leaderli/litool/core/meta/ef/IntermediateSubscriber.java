package io.leaderli.litool.core.meta.ef;

/**
 * 中间订阅器，实现了 SubscriberIf 和 SubscriptionIf 接口。用于将订阅者和订阅连接起来，接收请求并将通知数据和事件转发给实际的订阅者。
 *
 * @param <T> 订阅数据类型
 * @param <R> 完成事件的返回值类型
 */
abstract class IntermediateSubscriber<T, R> implements Subscriber<T, R>, Subscription {

    protected final Subscriber<? super T, R> actualSubscriber;
    private Subscription prevSubscription;

    protected IntermediateSubscriber(Subscriber<? super T, R> actualSubscriber) {
        this.actualSubscriber = actualSubscriber;
    }

    /**
     * 响应订阅信号，保存前一个订阅，并将自己注册到实际的订阅者中
     *
     * @param prevSubscription 前一个订阅
     */
    @Override
    public void onSubscribe(Subscription prevSubscription) {
        this.prevSubscription = prevSubscription;
        actualSubscriber.onSubscribe(this);
    }

    /**
     * 将数据传递给实际的订阅者
     *
     * @param t 订阅数据
     */
    @Override
    public void next(T t) {
        this.actualSubscriber.next(t);
    }

    /**
     * 将完成事件传递给实际的订阅者
     *
     * @param value 完成事件的返回值
     */
    @Override
    public void onComplete(R value) {
        this.actualSubscriber.onComplete(value);
    }

    /**
     * 发送请求给前一个订阅
     */
    @Override
    public void request() {
        this.prevSubscription.request();
    }

}
