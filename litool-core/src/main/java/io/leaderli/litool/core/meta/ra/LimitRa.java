package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.meta.Lino;

/**
 * 限制元素的个数
 *
 * @author leaderli
 * @since 2022/6/27
 */
public class LimitRa<T> extends SomeRa<T> {
    private final int limit;
    private final PublisherRa<T> prevPublisher;

    public LimitRa(PublisherRa<T> prevPublisher, int limit) {
        this.prevPublisher = prevPublisher;
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
        public void next(Lino<? extends T> t) {
            if (limit < 1) {
                this.cancel();

            } else {
                this.actualSubscriber.next(t);
                limit--;
            }
        }

    }
}
