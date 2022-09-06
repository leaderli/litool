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
class DropWhileRa<T> extends PublisherRa<T> {


    private final Function<? super T, ?> drop_condition;

    public DropWhileRa(Publisher<T> prevPublisher, Function<? super T, ?> drop_condition) {
        super(prevPublisher);
        this.drop_condition = drop_condition;
    }

    @Override
    public void subscribe(Subscriber<? super T> actualSubscriber) {
        prevPublisher.subscribe(new DropWhileSubscriberSubscription(actualSubscriber));

    }


    private final class DropWhileSubscriberSubscription extends IntermediateSubscriberSubscription<T, T> {

        private boolean dropped;


        public DropWhileSubscriberSubscription(Subscriber<? super T> actualSubscriber) {
            super(actualSubscriber);
        }


        @Override
        public void next_null() {
            //  filter will avoid null element
            dropped = drop_condition == null;
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
