package io.leaderli.litool.core.meta.ra;

/**
 * @author leaderli
 * @since 2022/6/27
 */
class NoneRa<T> extends PublisherRa<T> {

    public NoneRa(Publisher<T> prevPublisher) {
        super(prevPublisher);
    }

    @Override
    public void subscribe(Subscriber<? super T> actualSubscriber) {
        prevPublisher.subscribe(new NoneSubscriberSubscription<>(actualSubscriber));

    }

    private static final class NoneSubscriberSubscription<T> extends IntermediateSubscriberSubscription<T, T> {


        private NoneSubscriberSubscription(Subscriber<? super T> actualSubscriber) {
            super(actualSubscriber);
        }


        @Override
        public void request(int bit) {
            prevSubscription.cancel();
        }

        @Override
        public void next_null() {
            throw new IllegalStateException("NoneSubscriber will never call onNull");
        }

        @Override
        public void next(T t) {
            throw new IllegalStateException("NoneSubscriber will never call next");
        }
    }
}
