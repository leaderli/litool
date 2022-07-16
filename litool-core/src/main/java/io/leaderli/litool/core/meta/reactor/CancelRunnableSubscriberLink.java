package io.leaderli.litool.core.meta.reactor;

import io.leaderli.litool.core.meta.Lino;

/**
 * @author leaderli
 * @since 2022/7/16
 */
class CancelRunnableSubscriberLink<T> extends IntermediateSubscriberLink<T> {

    private final Runnable runnable;

    protected CancelRunnableSubscriberLink(SubscriberLink<T> actualSubscriber, Runnable runnable) {
        super(actualSubscriber);
        this.runnable = runnable;
    }


    @Override
    public void next(T value) {

        this.actualSubscriber.next(value);
    }

    @Override
    public void onCancel(Lino<T> lino) {
        runnable.run();
        this.actualSubscriber.onCancel(lino);
    }
}
