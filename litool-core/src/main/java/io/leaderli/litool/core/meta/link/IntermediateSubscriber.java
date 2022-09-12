package io.leaderli.litool.core.meta.link;

/**
 * 一个用于连接前后节点的中间 消费者，消费操作的抽象类
 *
 * @author leaderli
 * @since 2022/6/22
 */
abstract class IntermediateSubscriber<T, R> implements SubscriberLink<T>, SubscriptionLink {
    protected final SubscriberLink<R> actualSubscriber;
    protected SubscriptionLink prevSubscription;

    protected IntermediateSubscriber(SubscriberLink<R> actualSubscriber) {
        this.actualSubscriber = actualSubscriber;
    }


    @Override
    public void onSubscribe(SubscriptionLink prevSubscription) {
        this.prevSubscription = prevSubscription;
        actualSubscriber.onSubscribe(this);
    }


    @Override
    public void request() {
        this.prevSubscription.request();
    }


}
