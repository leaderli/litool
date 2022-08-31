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
public class DropWhileSome<T> extends PublisherSome<T> {


    private final Function<? super T, ?> filter;

    public DropWhileSome(Publisher<T> prevPublisher, Function<? super T, ?> filter) {
        super(prevPublisher);
        this.filter = filter;
    }

    @Override
    public void subscribe(Subscriber<? super T> actualSubscriber) {
        prevPublisher.subscribe(new TakeWhileSubscriber(actualSubscriber));

    }


    private final class TakeWhileSubscriber extends IntermediateSubscriber<T, T> {

        private boolean drop;

        public TakeWhileSubscriber(Subscriber<? super T> actualSubscriber) {
            super(actualSubscriber);
        }

        @Override
        public void next(T t) {

            if (drop) {
                actualSubscriber.next(t);
                return;
            }
            drop = BooleanUtil.parse(filter.apply(t));
            if (drop) {
                actualSubscriber.next(t);
            }

        }

        @Override
        public void onNull() {
            //  filter will avoid null element
        }
    }
}
