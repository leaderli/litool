package io.leaderli.litool.core.meta.reactor;

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


    private final Function<? super T, Object> filter;

    public FilterRa(PublisherRa<T> prevPublisher, Function<? super T, Object> filter) {
        this.prevPublisher = prevPublisher;
        this.filter = filter;
    }

    @Override
    public void subscribe(SubscriberRa<? super T> actualSubscriber) {
        prevPublisher.subscribe(new FilterSubscriberRa<>(actualSubscriber, filter));

    }


    private static class FilterSubscriberRa<T> extends IntermediateSubscriberRa<T, T> {
        private final Function<? super T, Object> filter;

        public FilterSubscriberRa(SubscriberRa<? super T> actualSubscriber, Function<? super T, Object> filter) {
            super(actualSubscriber);
            this.filter = filter;
        }

        @Override
        public void next(Lino<T> t) {

            t.filter(filter).nest(f -> this.actualSubscriber.next(Lino.narrow(f)));

        }

    }
}
