package io.leaderli.litool.core.meta.link;

import io.leaderli.litool.core.meta.Lino;

/**
 * 一个用于连接前后节点的中间 消费者，消费操作的抽象类
 *
 * @author leaderli
 * @since 2022/6/22
 */
public abstract class IntermediateSubscriberLink<T> implements SubscriberLink<T>, SubscriptionLink<T> {
    protected final SubscriberLink<T> actualSubscriber;
    protected SubscriptionLink<T> prevSubscription;

    protected IntermediateSubscriberLink(SubscriberLink<T> actualSubscriber) {
        this.actualSubscriber = actualSubscriber;
    }


    @Override
    public void onSubscribe(SubscriptionLink<T> prevSubscription) {
        this.prevSubscription = prevSubscription;
        actualSubscriber.onSubscribe(this);
    }


    @Override
    public void request() {
        this.prevSubscription.request();
    }

    @Override
    public void request(T t) {
        this.prevSubscription.request(t);
    }

    @Override
    public void onCancel(Lino<T> lino) {
        this.actualSubscriber.onCancel(lino);
    }
}
