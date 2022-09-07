package io.leaderli.litool.core.meta.ra;

/**
 * a  middle node which link subscriber and subscription which send event
 * to {@link  #prevSubscription}, notification data, event to {@link  #actualSubscriber}
 *
 * @author leaderli
 * @since 2022/6/22
 */
abstract class IntermediateSubscriberSubscription<T, R> implements Subscriber<T>, Subscription {
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
