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
        prevPublisher.subscribe(new DebugSubscriber(actualSubscriber));

    }

    private class DebugSubscriber extends IntermediateSubscriber<T, T> {


        private DebugSubscriber(Subscriber<? super T> actualSubscriber) {
            super(actualSubscriber);
        }


        @Override
        public void next(T t) {
            consumer.accept(t);
            actualSubscriber.next(t);
        }

        @Override
        public void onNull() {
            consumer.onNull();
            actualSubscriber.onNull();
        }

    }
}
