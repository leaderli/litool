package io.leaderli.litool.core.meta.ra;

/**
 * @author leaderli
 * @since 2022/8/30
 */
public class DebugSome<T> extends PublisherSome<T> {
    private final DebugConsumer<T> consumer;

    public DebugSome(Publisher<T> prevPublisher, DebugConsumer<T> consumer) {
        super(prevPublisher);
        this.consumer = consumer;
    }

    @Override
    public void subscribe(Subscriber<? super T> actualSubscriber) {
        prevPublisher.subscribe(new DebugSubscriberSubscription(actualSubscriber));

    }

    private class DebugSubscriberSubscription extends IntermediateSubscriberSubscription<T, T> {


        private DebugSubscriberSubscription(Subscriber<? super T> actualSubscriber) {
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
