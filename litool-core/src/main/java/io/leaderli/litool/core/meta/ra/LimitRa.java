package io.leaderli.litool.core.meta.ra;

/**
 * @author leaderli
 * @since 2022/6/27
 */
class LimitRa<T> extends RaWithPrevPublisher<T> {
    private final int limit;

    public LimitRa(PublisherRa<T> prevPublisher, int limit) {
        super(prevPublisher);
        this.limit = limit;
    }

    @Override
    public void subscribe(SubscriberRa<? super T> actualSubscriber) {
        prevPublisher.subscribe(new LimitSubscriberSubscription<>(actualSubscriber, limit));

    }

    private static final class LimitSubscriberSubscription<T> extends IntermediateSubscriberSubscription<T, T> {

        private int limit;

        private LimitSubscriberSubscription(SubscriberRa<? super T> actualSubscriber, int limit) {
            super(actualSubscriber);
            this.limit = limit;
        }


        @Override
        public void request(int states) {
            if (limit < 1) {
                this.cancel();
                return;
            }
            super.request(states | LiraBit.LIMIT);
        }

        @Override
        public void next_null() {
            if (limit < 1) {
                this.cancel();
                return;
            }
            this.actualSubscriber.next_null();
            limit--;
        }

        @Override
        public void next(T t) {

            if (limit < 1) {
                this.cancel();
                return;
            }
            this.actualSubscriber.next(t);
            limit--;
        }
    }
}
