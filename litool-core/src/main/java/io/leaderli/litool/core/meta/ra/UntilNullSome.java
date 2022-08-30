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
public class UntilNullSome<T> extends PublisherSome<T> {


    public UntilNullSome(Publisher<T> prevPublisher) {
        super(prevPublisher);
    }

    @Override
    public void subscribe(Subscriber<? super T> actualSubscriber) {
        prevPublisher.subscribe(new FilterSubscriber(actualSubscriber));

    }


    private final class FilterSubscriber extends IntermediateSubscriber<T, T> {

        public FilterSubscriber(Subscriber<? super T> actualSubscriber) {
            super(actualSubscriber);
        }

        @Override
        public void next(T t) {
            actualSubscriber.next(t);
        }

        @Override
        public void onNull() {
            cancel();
        }
    }
}
