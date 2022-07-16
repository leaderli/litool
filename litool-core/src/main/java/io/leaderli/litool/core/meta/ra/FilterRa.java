package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.meta.Lino;

import java.util.function.Function;

/**
 * 过滤不符合断言的元素
 *
 * @author leaderli
 * @see Lino#filter(Function)
 * @see io.leaderli.litool.core.util.LiBoolUtil#parse(Object)
 * @since 2022/6/27
 */
public class FilterRa<T> extends SomeRa<T> {
    private final PublisherRa<T> prevPublisher;


    private final Function<? super T, ?> filter;

    public FilterRa(PublisherRa<T> prevPublisher, Function<? super T, ?> filter) {
        this.prevPublisher = prevPublisher;
        this.filter = filter;
    }

    @Override
    public void subscribe(SubscriberRa<? super T> actualSubscriber) {
        prevPublisher.subscribe(new FilterSubscriberRa(actualSubscriber));

    }


    private final class FilterSubscriberRa extends IntermediateSubscriberRa<T, T> {

        public FilterSubscriberRa(SubscriberRa<? super T> actualSubscriber) {
            super(actualSubscriber);
        }

        @Override
        public void next(Lino<? extends T> t) {

            t.filter(filter).nest(this.actualSubscriber::next);

        }

    }
}
