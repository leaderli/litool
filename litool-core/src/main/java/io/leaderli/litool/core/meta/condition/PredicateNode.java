package io.leaderli.litool.core.meta.condition;

import io.leaderli.litool.core.util.BooleanUtil;

import java.util.Objects;
import java.util.function.Function;


class PredicateNode<T, R> extends LiThen<T, T, R> {
    private final Publisher<T, R> prevPublisher;
    private final Function<? super T, ?> filter;

    public PredicateNode(Publisher<T, R> prevPublisher, Function<? super T, ?> filter) {
        Objects.requireNonNull(filter);
        this.prevPublisher = prevPublisher;
        this.filter = filter;
    }

    @Override
    public void subscribe(Subscriber<? super T, R> actualSubscriber) {
        prevPublisher.subscribe(new SubscriberThen(actualSubscriber));
    }


    private class SubscriberThen extends IntermediateSubscriber<T, R> {

        public SubscriberThen(Subscriber<? super T, R> actualSubscriber) {
            super(actualSubscriber);
        }

        @Override
        public void next(T t) {

            if (BooleanUtil.parse(filter.apply(t))) {
                this.actualSubscriber.apply(t);
            } else {
                this.actualSubscriber.next(t);
            }
        }
    }
}
