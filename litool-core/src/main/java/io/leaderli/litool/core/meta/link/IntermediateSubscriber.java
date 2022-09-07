package io.leaderli.litool.core.meta.link;

/**
 * 一个用于连接前后节点的中间 消费者，消费操作的抽象类
 *
 * @author leaderli
 * @since 2022/6/22
 */
abstract class IntermediateSubscriber<T, R> implements Subscriber<T>, Subscription<R> {
    protected final Subscriber<R> actualSubscriber;
    protected Subscription<T> prevSubscription;

    protected IntermediateSubscriber(Subscriber<R> actualSubscriber) {
        this.actualSubscriber = actualSubscriber;
    }


    @Override
    public void onSubscribe(Subscription<T> prevSubscription) {
        this.prevSubscription = prevSubscription;
        actualSubscriber.onSubscribe(this);
    }


    @Override
    public void request() {
        this.prevSubscription.request();
    }


}
