package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.meta.Lino;

/**
 * 跳过前几个元素
 *
 * @author leaderli
 * @since 2022/6/27
 */
public class SkipRa<T> extends DefaultSomeRa<T> {
    private final int skip;

    public SkipRa(PublisherRa<T> prevPublisher, int skip) {
        super(prevPublisher);
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
        public void next(Lino<? extends T> t) {
            if (skip < 1) {

                this.actualSubscriber.next(t);
            } else {
                skip--;
            }
        }

    }
}
