package io.leaderli.litool.core.meta.ra;

/**
 * 一个用于连接前后节点的中间 消费者，消费操作的抽象类
 *
 * @author leaderli
 * @since 2022/6/22
 */
public abstract class IntermediateSubscriberSubscription<T, R> implements Subscriber<T>, Subscription {
    protected final Subscriber<? super R> actualSubscriber;
    Subscription prevSubscription;

    protected IntermediateSubscriberSubscription(Subscriber<? super R> actualSubscriber) {
        this.actualSubscriber = actualSubscriber;
    }

    @Override
    public void request(int bit) {
        this.prevSubscription.request(bit);

    }


    @Override
    public void cancel() {
        this.prevSubscription.cancel();

    }

    @Override
    public final void onSubscribe(Subscription prevSubscription) {
        this.prevSubscription = prevSubscription;
        actualSubscriber.onSubscribe(this);
    }

    @Override
    public void next_null() {
        this.actualSubscriber.next_null();
    }


    @Override
    public void onComplete() {
        this.actualSubscriber.onComplete();
    }

    @Override
    public void onCancel() {
        this.actualSubscriber.onCancel();
    }

    @Override
    public void onError(Throwable t, CancelSubscription cancel) {
        this.actualSubscriber.onError(t, cancel);
    }


}
