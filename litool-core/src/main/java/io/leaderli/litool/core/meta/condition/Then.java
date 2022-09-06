package io.leaderli.litool.core.meta.condition;

import io.leaderli.litool.core.util.BooleanUtil;

import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/17
 * <p>
 * 当条件满足时执行转换函数，后接 if 系列
 */
class Then<T, R> extends If<T, R> {

    private final Publisher<T, R> prevPublisher;
    private final Function<? super T, ? extends R> mapper;


    public Then(Publisher<T, R> prevPublisher, Function<? super T, ? extends R> mapper) {
        this.prevPublisher = prevPublisher;
        this.mapper = mapper;
    }


    @Override
    public void subscribe(Subscriber<T, R> actualSubscriber) {
        prevPublisher.subscribe(new SubscriberThen(actualSubscriber));

    }

    private class SubscriberThen extends IntermediateSubscriber<T, R> {

        public SubscriberThen(Subscriber<T, R> actualSubscriber) {
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
