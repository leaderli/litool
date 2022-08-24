package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.meta.Lino;

import java.util.Iterator;
import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/18
 */
public class FlatMapRa<T, R> extends SomeRa<R> {


    private final Function<? super T, Iterator<? extends R>> mapper;
    private final PublisherRa<T> prevPublisher;

    public FlatMapRa(PublisherRa<T> prevPublisher, Function<? super T, Iterator<? extends R>> mapper) {
        this.prevPublisher = prevPublisher;
        this.mapper = mapper;
    }

    @Override
    public void subscribe(SubscriberRa<? super R> actualSubscriber) {
        prevPublisher.subscribe(new FlatMapSubscriberRa(actualSubscriber));

    }

    private class FlatMapSubscriberRa extends IntermediateSubscriberRa<T, R> {


        private FlatMapSubscriberRa(SubscriberRa<? super R> actualSubscriber) {
            super(actualSubscriber);
        }


        @Override
        public void next(T t) {

            // 展开迭代器，依次对非空元素执行下一步操作
            Lino.of(t).map(mapper).ifPresent(it -> it.forEachRemaining(this.actualSubscriber::next));
        }

    }
}
