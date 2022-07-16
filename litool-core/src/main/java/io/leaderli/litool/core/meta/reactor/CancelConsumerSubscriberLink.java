package io.leaderli.litool.core.meta.reactor;

import io.leaderli.litool.core.meta.Lino;

import java.util.function.Consumer;

/**
 * @author leaderli
 * @since 2022/7/16
 */
class CancelConsumerSubscriberLink<T> extends IntermediateSubscriberLink<T> {

    private final Consumer<? super T> cancelConsumer;

    protected CancelConsumerSubscriberLink(SubscriberLink<T> actualSubscriber, Consumer<? super T> cancelConsumer) {
        super(actualSubscriber);
        this.cancelConsumer = cancelConsumer;
    }


    @Override
    public void next(T value) {

        this.actualSubscriber.next(value);
    }

    @Override
    public void onCancel(Lino<T> lino) {
        lino.ifPresent(cancelConsumer::accept);
        this.actualSubscriber.onCancel(lino);
    }
}
