package io.leaderli.litool.core.meta;

/**
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
