package io.leaderli.litool.core.meta.ra;

/**
 * 跳过前几个元素
 *
 * @author leaderli
 * @since 2022/6/27
 */
class SkipRa<T> extends RaWithPrevPublisher<T> {
    private final int skip;

    public SkipRa(PublisherRa<T> prevPublisher, int skip) {
        super(prevPublisher);
        this.skip = skip;
    }

    @Override
    public void subscribe(SubscriberRa<? super T> actualSubscriber) {
        prevPublisher.subscribe(new SkipSubscriberSubscription<>(actualSubscriber, skip));

    }

    private static class SkipSubscriberSubscription<T> extends IntermediateSubscriberSubscription<T, T> {

        private int skip;

        private SkipSubscriberSubscription(SubscriberRa<? super T> actualSubscriber, int skip) {
            super(actualSubscriber);
            this.skip = skip;
        }


        @Override
        public void next(T t) {
            if (skip < 1) {
                this.actualSubscriber.next(t);
            } else {
                skip--;
            }
        }

        @Override
        public void next_null() {
            if (skip < 1) {
                this.actualSubscriber.next_null();
            } else {
                skip--;
            }
        }
    }
}
