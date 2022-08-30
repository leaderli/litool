package io.leaderli.litool.core.meta.condition;

import io.leaderli.litool.core.util.BooleanUtil;

import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/17
 * <p>
 * 当条件满足时执行转换函数，后接 if 系列
 */
public class ThenIf<T, R> implements LiIf<T, R> {

    private final PublisherIf<T, R> prevPublisher;
    private final Function<? super T, ? extends R> mapper;


    public ThenIf(PublisherIf<T, R> prevPublisher, Function<? super T, ? extends R> mapper) {
        this.prevPublisher = prevPublisher;
        this.mapper = mapper;
    }


    public void subscribe(SubscriberIf<T, R> actualSubscriber) {
        prevPublisher.subscribe(new SubscriberThenIf(actualSubscriber));

    }

    private class SubscriberThenIf extends IntermediateSubscriberIf<T, R> {

        public SubscriberThenIf(SubscriberIf<T, R> actualSubscriber) {
            super(actualSubscriber);
        }


        /**
         * 对实际值进行断言，如果满足，值执行转换函数，并将结果保存，并终止执行，
         *
         * @param t         实际值
         * @param predicate 断言函数
         * @see #mapper
         */
        @Override
        public void next(T t, Function<? super T, ?> predicate) {

            if (t != null && BooleanUtil.parse(predicate.apply(t))) {
                this.onComplete(mapper.apply(t));
            } else {
                this.actualSubscriber.next(t, null);
            }
        }

    }
}
