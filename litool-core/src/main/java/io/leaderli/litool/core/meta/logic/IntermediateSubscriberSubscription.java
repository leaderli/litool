package io.leaderli.litool.core.meta.logic;

/**
 * 一个中间节点，连接了一个订阅者和一个订阅，将事件发送到prevSubscription，将通知数据和事件发送到actualSubscriber。
 *
 * @param <T> 订阅器订阅的数据类型
 * @see Subscriber
 * @see Subscription
 */
abstract class IntermediateSubscriberSubscription<T> implements Subscriber<T>, Subscription<T> {
    protected final Subscriber<T> actualSubscriber;
    private Subscription<T> prevSubscription;

    protected IntermediateSubscriberSubscription(Subscriber<T> actualSubscriber) {
        this.actualSubscriber = actualSubscriber;
    }


    @Override
    public final void onSubscribe(Subscription<T> prevSubscription) {
        this.prevSubscription = prevSubscription;
        actualSubscriber.onSubscribe(this);
    }

    @Override
    public void next(T t, boolean lastState) {
        this.actualSubscriber.next(t, lastState);
    }

    @Override
    public void onNot() {
    }

    @Override
    public final void onComplete(boolean result) {
        this.actualSubscriber.onComplete(result);
    }

    @Override
    public void request(T value) {
        this.prevSubscription.request(value);
    }

}
