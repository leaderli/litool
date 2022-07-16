package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.meta.Lino;

import java.util.function.Function;

/**
 * 将元素类型转换为另外一个类型
 *
 * @author leaderli
 * @since 2022/6/27
 */
public class MapRa<T, R> extends SomeRa<R> {
    private final Function<? super T, ? extends R> mapper;
    private final PublisherRa<T> prevPublisher;

    public MapRa(PublisherRa<T> prevPublisher, Function<? super T, ? extends R> mapper) {
        this.prevPublisher = prevPublisher;
        this.mapper = mapper;
    }

    @Override
    public void subscribe(SubscriberRa<? super R> actualSubscriber) {
        prevPublisher.subscribe(new MapSubscriberRa(actualSubscriber));

    }

    private class MapSubscriberRa extends IntermediateSubscriberRa<T, R> {


        private MapSubscriberRa(SubscriberRa<? super R> actualSubscriber) {
            super(actualSubscriber);
        }


        @Override
        public void next(Lino<? extends T> t) {
            t.map(mapper).nest(this.actualSubscriber::next);
        }

    }
}
