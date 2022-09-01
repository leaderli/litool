package io.leaderli.litool.core.meta.ra;

/**
 * @author leaderli
 * @since 2022/9/1 6:10 PM
 */
public abstract class BarricadeIntermediateSubscription<T, R> extends IntermediateSubscriber<T, R> {
    protected BarricadeSubscription<R> barricadeSubscription;

    protected BarricadeIntermediateSubscription(Subscriber<? super R> actualSubscriber) {
        super(actualSubscriber);
    }

    @Override
    public void request() {
        if (barricadeSubscription == null) {
            super.request();
        } else {
            barricadeSubscription.request();
        }
    }

    public Subscription prevSubscription() {
        return this;
    }

    @Override
    public void onNull() {

        // not provide an iterator to init a new  barricadeSubscription
    }

    @Override
    public void onComplete() {
        if (this.barricadeSubscription == null) {
            this.actualSubscriber.onComplete();
        } else {
            this.barricadeSubscription = null;
        }
    }
}
