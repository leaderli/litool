package io.leaderli.litool.core.meta.reactor;

import io.leaderli.litool.core.meta.Lino;

/**
 * 限制元素的个数
 *
 * @author leaderli
 * @since 2022/6/27
 */
public class LimitRa<T, R> extends SomeRa<T> {
    private final int skip;
    private final PublisherRa<T> prevPublisher;

    public LimitRa(PublisherRa<T> prevPublisher, int skip) {
        this.prevPublisher = prevPublisher;
        this.skip = skip;
    }

    @Override
    public void subscribe(SubscriberRa<? super T> actualSubscriber) {
        prevPublisher.subscribe(new LimitSubscriberRa<>(actualSubscriber, skip));

    }

    private static class LimitSubscriberRa<T> extends IntermediateSubscriberRa<T, T> {

        private int skip;

        private LimitSubscriberRa(SubscriberRa<? super T> actualSubscriber, int skip) {
            super(actualSubscriber);
            this.skip = skip;
        }


        @Override
        public void next(Lino<? extends T> t) {
            if (skip < 1) {
                this.cancel();

            } else {
                this.actualSubscriber.next(t);
                skip--;
            }
        }

    }
}
