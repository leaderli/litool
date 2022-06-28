package io.leaderli.litool.core.meta;

/**
 * 一个用于连接前后节点的中间 消费者，消费操作的抽象类
 *
 * @author leaderli
 * @since 2022/6/22
 */
public abstract class IntermediateRaSubscriber<T, R> implements RaSubscriber<T>, RaSubscription {
    protected final RaSubscriber<? super R> actualSubscriber;
    private RaSubscription prevSubscription;

    public IntermediateRaSubscriber(RaSubscriber<? super R> actualSubscriber) {
        this.actualSubscriber = actualSubscriber;
    }

    @Override
    public final void request(int n) {
        this.prevSubscription.request(n);

    }

    @Override
    public final void cancel() {
        this.prevSubscription.cancel();

    }

    @Override
    public void onSubscribe(RaSubscription prevSubscription) {
        this.prevSubscription = prevSubscription;
        actualSubscriber.onSubscribe(this);
    }


    @Override
    public void onComplete() {

    }
}
