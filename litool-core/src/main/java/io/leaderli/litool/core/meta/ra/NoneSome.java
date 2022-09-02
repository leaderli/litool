package io.leaderli.litool.core.meta.ra;

/**
 * @author leaderli
 * @since 2022/6/27
 */
public class NoneSome<T> extends PublisherSome<T> {

    public NoneSome(Publisher<T> prevPublisher) {
        super(prevPublisher);
    }

    @Override
    public void subscribe(Subscriber<? super T> actualSubscriber) {
        prevPublisher.subscribe(new NoneSubscriber<>(actualSubscriber));

    }

    private static final class NoneSubscriber<T> extends IntermediateSubscriber<T, T> {


        private NoneSubscriber(Subscriber<? super T> actualSubscriber) {
            super(actualSubscriber);
        }


        @Override
        public void request(LiraBit bit) {
            prevSubscription.cancel();
        }

        @Override
        public void onNull() {
            throw new IllegalStateException("NoneSubscriber will never call onNull");
        }

        @Override
        public void next(T t) {
            throw new IllegalStateException("NoneSubscriber will never call next");
        }
    }
}
