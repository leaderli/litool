package io.leaderli.litool.core.meta.link;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.util.BooleanUtil;

import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/16
 */
class FilterLink<T> extends SomeLink<T, T> {


    private final Function<? super T, ?> filter;

    public FilterLink(PublisherLink<T> prevPublisher, Function<? super T, ?> filter) {
        super(prevPublisher);
        this.filter = filter;
    }


    @Override
    public void subscribe(SubscriberLink<T> actualSubscriber) {
        prevPublisher.subscribe(new FilterSubscriber(actualSubscriber));

    }

    /**
     * @author leaderli
     * @since 2022/7/16
     */
    private class FilterSubscriber extends SameTypeIntermediateSubscriber<T> {

        public FilterSubscriber(SubscriberLink<T> actualSubscriber) {
            super(actualSubscriber);
        }

        @Override
        public void next(T value) {

            boolean next = BooleanUtil.parse(filter.apply(value));


            if (next) {
                this.actualSubscriber.next(value);
            } else {
                this.actualSubscriber.onError(Lino.of(value));
            }
        }
    }
}
