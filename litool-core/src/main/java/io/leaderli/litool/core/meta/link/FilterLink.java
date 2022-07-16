package io.leaderli.litool.core.meta.link;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.SomeLink;
import io.leaderli.litool.core.util.LiBoolUtil;

import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/16
 */
public class FilterLink<T> extends SomeLink<T> {


    private final Function<? super T, ?> filter;

    public FilterLink(PublisherLink<T> prevPublisher, Function<? super T, ?> filter) {
        super(prevPublisher);
        this.filter = filter;
    }


    @Override
    public void subscribe(SubscriberLink<T> actualSubscriber) {
        prevPublisher.subscribe(new FilterSubscriberLink(actualSubscriber));

    }

    /**
     * @author leaderli
     * @since 2022/7/16
     */
    private class FilterSubscriberLink extends IntermediateSubscriberLink<T> {

        public FilterSubscriberLink(SubscriberLink<T> actualSubscriber) {
            super(actualSubscriber);
        }

        @Override
        public void next(T value) {

            boolean next = LiBoolUtil.parse(filter.apply(value));


            if (next) {
                this.actualSubscriber.next(value);
            } else {
                this.actualSubscriber.onCancel(Lino.of(value));
            }
        }
    }
}
