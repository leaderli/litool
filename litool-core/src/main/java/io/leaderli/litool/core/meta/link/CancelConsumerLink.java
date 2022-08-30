package io.leaderli.litool.core.meta.link;

import io.leaderli.litool.core.meta.Lino;

import java.util.function.Consumer;

/**
 * @author leaderli
 * @since 2022/7/16
 */
public class CancelConsumerLink<T> extends SomeLink<T, T> {

    private final Consumer<? super T> cancelConsumer;


    public CancelConsumerLink(PublisherLink<T> prevPublisher, final Consumer<? super T> cancelConsumer) {
        super(prevPublisher);
        this.cancelConsumer = cancelConsumer;
    }


    @Override
    public void subscribe(SubscriberLink<T> actualSubscriber) {
        prevPublisher.subscribe(new CancelConsumerSubscriberLink(actualSubscriber));
    }


    private class CancelConsumerSubscriberLink extends SameTypeIntermediateSubscriberLink<T> implements ErrorLink {


        protected CancelConsumerSubscriberLink(SubscriberLink<T> actualSubscriber) {
            super(actualSubscriber);
        }


        @Override
        public void next(T value) {

            this.actualSubscriber.next(value);
        }

        @Override
        public void onCancel(Lino<T> lino) {
            lino.ifPresent(cancelConsumer);

            if (this.actualSubscriber instanceof ErrorLink) {
                this.actualSubscriber.onCancel(lino);
            }
        }
    }
}
