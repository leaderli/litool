package io.leaderli.litool.core.meta.ra;

/**
 * @author leaderli
 * @since 2022/8/30
 */
class DebugRa<T> extends RaWithPrevPublisher<T> {
    private final DebugConsumer<T> consumer;

    public DebugRa(PublisherRa<T> prevPublisher, DebugConsumer<T> consumer) {
        super(prevPublisher);
        this.consumer = consumer;
    }

    @Override
    public void subscribe(SubscriberRa<? super T> actualSubscriber) {
        prevPublisher.subscribe(new DebugSubscriberSubscription(actualSubscriber));

    }

    private class DebugSubscriberSubscription extends IntermediateSubscriberSubscription<T, T> {


        private DebugSubscriberSubscription(SubscriberRa<? super T> actualSubscriber) {
            super(actualSubscriber);
        }


        @Override
        public void next(T t) {
            consumer.accept(t);
            actualSubscriber.next(t);
        }

        @Override
        public void next_null() {
            consumer.onNull();
            actualSubscriber.next_null();
        }

    }
}
