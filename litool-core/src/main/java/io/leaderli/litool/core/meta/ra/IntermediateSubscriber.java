package io.leaderli.litool.core.meta.ra;

/**
 * 一个用于连接前后节点的中间 消费者，消费操作的抽象类
 *
 * @author leaderli
 * @since 2022/6/22
 */
public abstract class IntermediateSubscriber<T, R> implements Subscriber<T>, Subscription {
    protected final Subscriber<? super R> actualSubscriber;
    Subscription prevSubscription;

    protected IntermediateSubscriber(Subscriber<? super R> actualSubscriber) {
        this.actualSubscriber = actualSubscriber;
    }

    @Override
    public void request(long num) {
        this.prevSubscription.request(num);

    }

    @Override
    public final void cancel() {
        this.prevSubscription.cancel();

    }

    @Override
    public Subscription prevSubscription() {
        return this.prevSubscription;
    }

    @Override
    public void onSubscribe(Subscription prevSubscription) {
        this.prevSubscription = prevSubscription.prevSubscription();
        actualSubscriber.onSubscribe(this);
    }

    @Override
    public void onNull() {
        this.actualSubscriber.onNull();
    }

    @Override
    public void onRequested() {
        this.actualSubscriber.onRequested();
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
