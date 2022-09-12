package io.leaderli.litool.core.meta.logic;

/**
 * a  middle node which link subscriber and subscription which send event
 * to {@link  #prevSubscription}, notification data, event to {@link  #actualSubscriber}
 *
 * @author leaderli
 * @since 2022/6/22
 */
abstract class IntermediateSubscriberSubscription<T> implements SubscriberLogic<T>, SubscriptionLogic<T> {
    protected final SubscriberLogic<T> actualSubscriber;
    private SubscriptionLogic<T> prevSubscription;

    protected IntermediateSubscriberSubscription(SubscriberLogic<T> actualSubscriber) {
        this.actualSubscriber = actualSubscriber;
    }


    @Override
    public final void onSubscribe(SubscriptionLogic<T> prevSubscription) {
        this.prevSubscription = prevSubscription;
        actualSubscriber.onSubscribe(this);
    }

    @Override
    public void next(T t, boolean last) {
        this.actualSubscriber.next(t, last);
    }

    @Override
    public void onNot() {
    }

    @Override
    public final void onComplete(boolean result) {
        this.actualSubscriber.onComplete(result);
    }

    @Override
    public void request(T value) {
        this.prevSubscription.request(value);
    }

}
