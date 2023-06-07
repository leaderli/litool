package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.meta.LiTuple;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/6/27
 */
public class TupleRa<T, R> extends Ra<LiTuple<T, R>> {
    private final Function<? super T, ? extends R> mapper;
    private final PublisherRa<T> prevPublisher;

    public TupleRa(PublisherRa<T> prevPublisher, Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper);
        this.prevPublisher = prevPublisher;
        this.mapper = mapper;
    }


    @Override
    public void subscribe(SubscriberRa<? super LiTuple<T, R>> actualSubscriber) {
        prevPublisher.subscribe(new MapSubscriberSubscription(actualSubscriber));

    }

    private class MapSubscriberSubscription extends IntermediateSubscriberSubscription<T, LiTuple<T, R>> {


        private MapSubscriberSubscription(SubscriberRa<? super LiTuple<T, R>> actualSubscriber) {
            super(actualSubscriber);
        }

        @Override
        public void next(T t) {
            SubscriberUtil.next(this.actualSubscriber, LiTuple.of(t, mapper.apply(t)));
        }

    }
}
