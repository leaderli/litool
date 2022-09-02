package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.exception.InfinityException;
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

    private static final int MAX_GENERATOR_TRY_COUNT = 256;

    private final Function<? super T, ?> filter;

    public DropWhileSome(Publisher<T> prevPublisher, Function<? super T, ?> filter) {
        super(prevPublisher);
        this.filter = filter;
    }

    @Override
    public void subscribe(Subscriber<? super T> actualSubscriber) {
        prevPublisher.subscribe(new DropWhileSubscriber(actualSubscriber));

    }


    private final class DropWhileSubscriber extends IntermediateSubscriber<T, T> {

        private boolean drop;

        private int count;

        public DropWhileSubscriber(Subscriber<? super T> actualSubscriber) {
            super(actualSubscriber);
        }

        @Override
        public void request(LiraBit bit) {
            bit.enable(LiraBit.T_DROP);
            super.request(bit);
        }

        @Override
        public void next(T t) {

            count++;
            if (count > MAX_GENERATOR_TRY_COUNT) {
                cancel();
                throw new InfinityException("max generator try count\r\n\tat " + this);
            }
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
