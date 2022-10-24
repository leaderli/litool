package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.util.BooleanUtil;

import java.util.Objects;
import java.util.function.Function;

/**
 * 过滤不符合断言的元素
 *
 * @author leaderli
 * @see Lino#filter(Function)
 * @see BooleanUtil#parse(Object)
 * @since 2022/6/27
 */
class TakeWhileRa<T> extends RaWithPrevPublisher<T> {


    private final Function<? super T, ?> filter;

    public TakeWhileRa(PublisherRa<T> prevPublisher, Function<? super T, ?> filter) {
        super(prevPublisher);
        Objects.requireNonNull(filter);
        this.filter = filter;
    }

    @Override
    public void subscribe(SubscriberRa<? super T> actualSubscriber) {
        prevPublisher.subscribe(new TakeWhileSubscriberSubscription(actualSubscriber));

    }


    private final class TakeWhileSubscriberSubscription extends IntermediateSubscriberSubscription<T, T> {

        public TakeWhileSubscriberSubscription(SubscriberRa<? super T> actualSubscriber) {
            super(actualSubscriber);
        }


        @Override
        public void next(T t) {

            boolean present = BooleanUtil.parse(filter.apply(t));
            if (present) {
                cancel();
                return;
            }
            actualSubscriber.next(t);

        }

        @Override
        public void next_null() {
            //  filter will avoid null element
            if (filter instanceof NullableFunction) {
                if (BooleanUtil.parse(filter.apply(null))) {
                    cancel();
                }
            }
            super.next_null();
        }
    }
}
