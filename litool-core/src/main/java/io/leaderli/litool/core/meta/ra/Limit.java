package io.leaderli.litool.core.meta.ra;

/**
 * 限制元素的个数
 *
 * @author leaderli
 * @since 2022/6/27
 */
public class Limit<T> extends DefaultSome<T> {
    private final int limit;

    public Limit(Publisher<T> prevPublisher, int limit) {
        super(prevPublisher);
        this.limit = limit;
    }

    @Override
    public void subscribe(Subscriber<? super T> actualSubscriber) {
        prevPublisher.subscribe(new LimitSubscriber<>(actualSubscriber, limit));

    }

    private static final class LimitSubscriber<T> extends IntermediateSubscriber<T, T> {

        private int limit;

        private LimitSubscriber(Subscriber<? super T> actualSubscriber, int limit) {
            super(actualSubscriber);
            this.limit = limit;
        }


        @Override
        public void next(T t) {
            if (limit < 1) {
                this.cancel();

            } else {
                this.actualSubscriber.next(t);
                limit--;
            }
        }

    }
}
