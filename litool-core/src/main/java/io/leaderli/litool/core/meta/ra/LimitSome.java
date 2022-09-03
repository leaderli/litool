package io.leaderli.litool.core.meta.ra;

/**
 * 限制元素的个数
 *
 * @author leaderli
 * @since 2022/6/27
 */
public class LimitSome<T> extends PublisherSome<T> {
    private final int limit;

    public LimitSome(Publisher<T> prevPublisher, int limit) {
        super(prevPublisher);
        this.limit = limit;
    }

    @Override
    public void subscribe(Subscriber<? super T> actualSubscriber) {
        prevPublisher.subscribe(new LimitSubscriberSubscription<>(actualSubscriber, limit));

    }

    private static final class LimitSubscriberSubscription<T> extends IntermediateSubscriberSubscription<T, T> {

        private int limit;

        private LimitSubscriberSubscription(Subscriber<? super T> actualSubscriber, int limit) {
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
