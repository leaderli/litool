package io.leaderli.litool.core.meta.condition;

import java.util.function.Consumer;

/**
 * @author leaderli
 * @since 2022/6/22
 */
public abstract class IntermediateSubscriberIf<T, R> implements SubscriberIf<T, R>, SubscriptionIf<R> {
    protected final SubscriberIf<T, R> actualSubscriber;
    private SubscriptionIf<R> prevSubscription;

    protected IntermediateSubscriberIf(SubscriberIf<T, R> actualSubscriber) {
        this.actualSubscriber = actualSubscriber;
    }

    @Override
    public void onSubscribe(SubscriptionIf<R> prevSubscription) {
        this.prevSubscription = prevSubscription;
        actualSubscriber.onSubscribe(this);
    }


    @Override
    public void request(Consumer<? super R> completeConsumer) {
        this.prevSubscription.request(completeConsumer);

    }

    @Override
    public void onComplete(R value) {
        this.prevSubscription.onComplete(value);
    }
}
