package io.leaderli.litool.core.meta.reactor;

/**
 * 一个用于连接前后节点的中间 消费者，消费操作的抽象类
 *
 * @author leaderli
 * @since 2022/6/22
 */
public abstract class IntermediateSubscriberRa<T, R> implements SubscriberRa<T>, SubscriptionRa {
    protected final SubscriberRa<? super R> actualSubscriber;
    private SubscriptionRa prevSubscription;

    public IntermediateSubscriberRa(SubscriberRa<? super R> actualSubscriber) {
        this.actualSubscriber = actualSubscriber;
    }

    @Override
    public final void request() {
        this.prevSubscription.request();

    }

    @Override
    public final void cancel() {
        this.prevSubscription.cancel();

    }

    @Override
    public void onSubscribe(SubscriptionRa prevSubscription) {
        this.prevSubscription = prevSubscription;
        actualSubscriber.onSubscribe(this);
    }


    @Override
    public void onComplete() {

    }
}
