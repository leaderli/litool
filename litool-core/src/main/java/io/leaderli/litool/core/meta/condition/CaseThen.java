package io.leaderli.litool.core.meta.condition;

import io.leaderli.litool.core.util.LiBoolUtil;

import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/17
 */
public
class CaseThen<T, M, R> implements LiIf<T, R> {

    private final PublisherIf<T, R> prevPublisher;
    private final Function<? super M, ? extends R> mapper;


    public CaseThen(PublisherIf<T, R> prevPublisher, Function<? super M, ? extends R> mapper) {
        this.prevPublisher = prevPublisher;
        this.mapper = mapper;
    }


    public void subscribe(SubscriberIf<T, R> actualSubscriber) {
        prevPublisher.subscribe(new CaseThenSubscriberIf<>(mapper, actualSubscriber));

    }

    private static class CaseThenSubscriberIf<T, M, R> extends IntermediateSubscriberIf<T, R> {
        private final Function<? super M, ? extends R> mapper;

        public CaseThenSubscriberIf(Function<? super M, ? extends R> mapper, SubscriberIf<T, R> actualSubscriber) {
            super(actualSubscriber);
            this.mapper = mapper;

        }


        /**
         * 对实际值进行断言，如果满足，值执行转换函数，并将结果保存，并终止执行，
         *
         * @param t         实际值，该值一定 instanceof M
         * @param predicate 断言函数，此处一定为 {@link io.leaderli.litool.core.meta.condition.CaseWhen.CaseWhenSubscriberIf#next(Object, Function)}
         * @see #mapper
         * @see LiBoolUtil#parse(Object)
         */
        @SuppressWarnings({"unchecked", "JavadocReference"})
        @Override
        public void next(T t, Function<? super T, ?> predicate) {

            if (t != null && LiBoolUtil.parse(predicate.apply(t))) {
                this.onComplete(this.mapper.apply((M) t));
            } else {
                this.actualSubscriber.next(t, null);
            }
        }

    }
}
