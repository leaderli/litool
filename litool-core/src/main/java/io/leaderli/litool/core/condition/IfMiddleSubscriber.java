package io.leaderli.litool.core.condition;

import io.leaderli.litool.core.meta.LiBox;

/**
 * @author leaderli
 * @since 2022/6/22
 */
public abstract class IfMiddleSubscriber<T, R> implements IfSubscriber<T, R>, IfSubscription<R> {
    protected final IfSubscriber<T, R> actualSubscriber;
    private IfSubscription<R> prevSubscription;

    public IfMiddleSubscriber(IfSubscriber<T, R> actualSubscriber) {
        this.actualSubscriber = actualSubscriber;
    }

    @Override
    public void onSubscribe(IfSubscription<R> prevSubscription) {
        this.prevSubscription = prevSubscription;
        actualSubscriber.onSubscribe(this);
    }


    @Override
    public void request(LiBox<R> box) {
        this.prevSubscription.request(box);

    }

    @Override
    public void onComplete(R value) {
        this.prevSubscription.onComplete(value);
    }
}
