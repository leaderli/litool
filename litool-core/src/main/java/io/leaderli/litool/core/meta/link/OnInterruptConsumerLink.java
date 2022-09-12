package io.leaderli.litool.core.meta.link;

import java.util.function.Consumer;

/**
 * @author leaderli
 * @since 2022/7/16
 */
class OnInterruptConsumerLink<T> extends SomeLink<T, T> {

    private final Consumer<? super T> errorConsumer;


    public OnInterruptConsumerLink(PublisherLink<T> prevPublisher, final Consumer<? super T> errorConsumer) {
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
        public void onInterrupt(T value) {
            if (value != null) {
                errorConsumer.accept(value);
            }

            if (this.actualSubscriber instanceof OnErrorSubscriber) {
                this.actualSubscriber.onInterrupt(value);
            }
        }
    }
}
