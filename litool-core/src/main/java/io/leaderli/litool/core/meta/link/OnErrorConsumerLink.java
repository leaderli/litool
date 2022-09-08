package io.leaderli.litool.core.meta.link;

import io.leaderli.litool.core.meta.Lino;

import java.util.function.Consumer;

/**
 * @author leaderli
 * @since 2022/7/16
 */
public class OnErrorConsumerLink<T> extends SomeLink<T, T> {

    private final Consumer<? super T> errorConsumer;


    public OnErrorConsumerLink(PublisherLink<T> prevPublisher, final Consumer<? super T> errorConsumer) {
        super(prevPublisher);
        this.errorConsumer = errorConsumer;
    }


    @Override
    public void subscribe(SubscriberLink<T> actualSubscriber) {
        prevPublisher.subscribe(new CancelConsumerSubscriber(actualSubscriber));
    }


    private class CancelConsumerSubscriber extends SameTypeIntermediateSubscriber<T> implements OnErrorSubscriber {


        protected CancelConsumerSubscriber(SubscriberLink<T> actualSubscriber) {
            super(actualSubscriber);
        }


        @Override
        public void next(T value) {

            this.actualSubscriber.next(value);
        }

        @Override
        public void onError(Lino<T> lino) {
            lino.ifPresent(errorConsumer);

            if (this.actualSubscriber instanceof OnErrorSubscriber) {
                this.actualSubscriber.onError(lino);
            }
        }
    }
}
