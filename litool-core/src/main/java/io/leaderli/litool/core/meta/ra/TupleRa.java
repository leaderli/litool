package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.meta.LiTuple2;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/6/27
 */
public class TupleRa<T, R> extends Ra<LiTuple2<T, R>> {
    private final Function<? super T, ? extends R> mapper;
    private final PublisherRa<T> prevPublisher;

    public TupleRa(PublisherRa<T> prevPublisher, Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper);
        this.prevPublisher = prevPublisher;
        this.mapper = mapper;
    }


    @Override
    public void subscribe(SubscriberRa<? super LiTuple2<T, R>> actualSubscriber) {
        prevPublisher.subscribe(new MapSubscriberSubscription(actualSubscriber));

    }

    private class MapSubscriberSubscription extends IntermediateSubscriberSubscription<T, LiTuple2<T, R>> {


        private MapSubscriberSubscription(SubscriberRa<? super LiTuple2<T, R>> actualSubscriber) {
            super(actualSubscriber);
        }

        @Override
        public void next(T t) {
            SubscriberUtil.next(this.actualSubscriber, LiTuple.of(t, mapper.apply(t)));
        }

    }
}
