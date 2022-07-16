package io.leaderli.litool.core.meta.reactor;

import io.leaderli.litool.core.meta.Lino;

/**
 * 跳过前几个元素
 *
 * @author leaderli
 * @since 2022/6/27
 */
public class SkipRa<T, R> extends SomeRa<T> {
    private final int skip;
    private final PublisherRa<T> prevPublisher;

    public SkipRa(PublisherRa<T> prevPublisher, int skip) {
        this.prevPublisher = prevPublisher;
        this.skip = skip;
    }

    @Override
    public void subscribe(SubscriberRa<? super T> actualSubscriber) {
        prevPublisher.subscribe(new SkipSubscriberRa<>(actualSubscriber, skip));

    }

    private static class SkipSubscriberRa<T> extends IntermediateSubscriberRa<T, T> {

        private int skip;

        private SkipSubscriberRa(SubscriberRa<? super T> actualSubscriber, int skip) {
            super(actualSubscriber);
            this.skip = skip;
        }


        @Override
        public void next(Lino<T> t) {
            if (skip < 1) {

                this.actualSubscriber.next(Lino.narrow(t));
            } else {
                skip--;
            }
        }

    }
}
