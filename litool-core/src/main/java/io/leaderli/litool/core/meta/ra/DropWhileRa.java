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
class DropWhileRa<T> extends RaWithPrevPublisher<T> {


    private final Function<? super T, ?> drop_condition;

    public DropWhileRa(PublisherRa<T> prevPublisher, Function<? super T, ?> drop_condition) {
        super(prevPublisher);
        Objects.requireNonNull(drop_condition);
        this.drop_condition = drop_condition;
    }

    @Override
    public void subscribe(SubscriberRa<? super T> actualSubscriber) {
        prevPublisher.subscribe(new DropWhileSubscriberSubscription(actualSubscriber));

    }


    private final class DropWhileSubscriberSubscription extends IntermediateSubscriberSubscription<T, T> {

        private boolean dropped;


        public DropWhileSubscriberSubscription(SubscriberRa<? super T> actualSubscriber) {
            super(actualSubscriber);
        }


        @Override
        public void next_null() {
            //  filter will avoid null element

            if (dropped) {
                actualSubscriber.next_null();
                return;
            }
            if (drop_condition instanceof NullableFunction) {
                dropped = BooleanUtil.parse(drop_condition.apply(null));
            }
            if (dropped) {
                actualSubscriber.next_null();
            }
        }

        @Override
        public void onComplete() {
            dropped = true;
            actualSubscriber.onComplete();
        }

        @Override
        public void next(T t) {

            if (dropped) {
                actualSubscriber.next(t);
                return;
            }
            dropped = BooleanUtil.parse(drop_condition.apply(t));
            if (dropped) {
                actualSubscriber.next(t);
            }

        }
    }
}
