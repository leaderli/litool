package io.leaderli.litool.core.meta.ra;

/**
 * 限制元素的个数
 *
 * @author leaderli
 * @since 2022/6/27
 */
public class LimitRa<T> extends DefaultSomeRa<T> {
    private final int limit;

    public LimitRa(PublisherRa<T> prevPublisher, int limit) {
        super(prevPublisher);
        this.limit = limit;
    }

    @Override
    public void subscribe(SubscriberRa<? super T> actualSubscriber) {
        prevPublisher.subscribe(new LimitSubscriberRa<>(actualSubscriber, limit));

    }

    private static final class LimitSubscriberRa<T> extends IntermediateSubscriberRa<T, T> {

        private int limit;

        private LimitSubscriberRa(SubscriberRa<? super T> actualSubscriber, int limit) {
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
