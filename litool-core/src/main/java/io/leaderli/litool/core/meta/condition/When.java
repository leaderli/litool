package io.leaderli.litool.core.meta.condition;

import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/17
 */
public
class When<T, R> implements LiThen<T, R> {
    private final PublisherIf<T, R> prevPublisher;
    private final Function<? super T, ?> filter;

    public When(PublisherIf<T, R> prevPublisher, Function<? super T, ?> filter) {
        this.prevPublisher = prevPublisher;
        this.filter = filter;
    }

    @Override
    public void subscribe(SubscriberIf<T, R> actualSubscriber) {
        prevPublisher.subscribe(new WhenSubscriberIf<>(filter, actualSubscriber));

    }

    private static class WhenSubscriberIf<T, R> extends IntermediateSubscriberIf<T, R> {
        private final Function<? super T, ?> predicate;

        public WhenSubscriberIf(Function<? super T, ?> predicate, SubscriberIf<T, R> actualSubscriber) {
            super(actualSubscriber);
            this.predicate = predicate;

        }


        @Override
        public void next(T t, Function<? super T, ?> predicate) {

            this.actualSubscriber.next(t, this.predicate);
        }

    }
}
