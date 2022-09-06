package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.util.BooleanUtil;

import java.util.function.Function;

/**
 * 过滤不符合断言的元素
 *
 * @author leaderli
 * @see Lino#filter(Function)
 * @see BooleanUtil#parse(Object)
 * @since 2022/6/27
 */
class FilterRa<T> extends PublisherRa<T> {


    private final Function<? super T, ?> filter;

    public FilterRa(Publisher<T> prevPublisher, Function<? super T, ?> filter) {
        super(prevPublisher);
        this.filter = filter;
    }

    @Override
    public void subscribe(Subscriber<? super T> actualSubscriber) {
        prevPublisher.subscribe(new FilterSubscriberSubscription(actualSubscriber));

    }


    private final class FilterSubscriberSubscription extends IntermediateSubscriberSubscription<T, T> {

        public FilterSubscriberSubscription(Subscriber<? super T> actualSubscriber) {
            super(actualSubscriber);
        }

        @Override
        public void next(T t) {
            if (filter != null) {
                if (BooleanUtil.parse(filter.apply(t))) {
                    this.actualSubscriber.next(t);
                }
            } else {
                this.actualSubscriber.next(t);
            }
        }

        @Override
        public void next_null() {
            //  filter will avoid null element
            if (filter == null) {
                this.actualSubscriber.next_null();
            }
        }
    }
}
