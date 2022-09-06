package io.leaderli.litool.core.meta.condition;

import io.leaderli.litool.core.util.BooleanUtil;

import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/17
 */
class Case<T, M, R> extends If<T, R> {

    private final Publisher<T, R> prevPublisher;
    private final Function<? super M, ? extends R> mapper;


    public Case(Publisher<T, R> prevPublisher, Function<? super M, ? extends R> mapper) {
        this.prevPublisher = prevPublisher;
        this.mapper = mapper;
    }


    @Override
    public void subscribe(Subscriber<T, R> actualSubscriber) {
        prevPublisher.subscribe(new SubscriberCase(actualSubscriber));

    }

    private class SubscriberCase extends IntermediateSubscriber<T, R> {

        public SubscriberCase(Subscriber<T, R> actualSubscriber) {
            super(actualSubscriber);

        }


        /**
         * 对实际值进行断言，如果满足，值执行转换函数，并将结果保存，并终止执行，
         *
         * @param t         实际值，该值一定 instanceof M
         * @param predicate 断言函数，此处一定为 {@link InstanceOfThen.CaseWhenSubscriberIf#next(Object, Function)}
         * @see #mapper
         * @see BooleanUtil#parse(Object)
         */
        @SuppressWarnings({"unchecked", "JavadocReference"})
        @Override
        public void next(T t, Function<? super T, ?> predicate) {

            if (t != null && BooleanUtil.parse(predicate.apply(t))) {
                this.onComplete(mapper.apply((M) t));
            } else {
                this.actualSubscriber.next(t, null);
            }
        }

    }
}
