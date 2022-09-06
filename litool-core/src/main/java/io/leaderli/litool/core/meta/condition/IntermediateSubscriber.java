package io.leaderli.litool.core.meta.condition;

import java.util.function.Consumer;

/**
 * @author leaderli
 * @since 2022/6/22
 */
abstract class IntermediateSubscriber<T, R> implements Subscriber<T, R>, Subscription<R> {
    protected final Subscriber<T, R> actualSubscriber;
    private Subscription<R> prevSubscription;

    protected IntermediateSubscriber(Subscriber<T, R> actualSubscriber) {
        this.actualSubscriber = actualSubscriber;
    }

    @Override
    public void onSubscribe(Subscription<R> prevSubscription) {
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
