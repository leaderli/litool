package io.leaderli.litool.core.meta.ef;

/**
 * a  middle node which link subscriber and subscription which send request
 * to {@link  #prevSubscription}, notification data, event to {@link  #actualSubscriber}
 *
 * @author leaderli
 * @since 2022/6/22
 */
abstract class IntermediateSubscriber<T, R> implements SubscriberIf<T, R>, SubscriptionIf {
    protected final SubscriberIf<? super T, R> actualSubscriber;
    private SubscriptionIf prevSubscription;

    protected IntermediateSubscriber(SubscriberIf<? super T, R> actualSubscriber) {
        this.actualSubscriber = actualSubscriber;
    }

    @Override
    public void onSubscribe(SubscriptionIf prevSubscription) {
        this.prevSubscription = prevSubscription;
        actualSubscriber.onSubscribe(this);
    }

    @Override
    public void next(T t) {
        this.actualSubscriber.next(t);
    }

    @Override
    public void onComplete(R value) {
        this.actualSubscriber.onComplete(value);
    }

    @Override
    public void request() {
        this.prevSubscription.request();
    }

}
