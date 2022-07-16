package io.leaderli.litool.core.meta.condition;

import io.leaderli.litool.core.type.LiClassUtil;

import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/17
 */
public
class CaseWhen<T, M, R> implements LiCaseThen<T, M, R> {
    private final PublisherIf<T, R> prevPublisher;
    private final Class<? extends M> middleType;

    public CaseWhen(PublisherIf<T, R> prevPublisher, Class<? extends M> middleType) {
        this.prevPublisher = prevPublisher;
        this.middleType = middleType;
    }

    @Override
    public void subscribe(SubscriberIf<T, R> actualSubscriber) {
        prevPublisher.subscribe(new CaseWhenSubscriberIf(actualSubscriber));

    }

    private class CaseWhenSubscriberIf extends IntermediateSubscriberIf<T, R> {

        public CaseWhenSubscriberIf(SubscriberIf<T, R> actualSubscriber) {
            super(actualSubscriber);

        }


        @Override
        public void next(T t, Function<? super T, ?> predicate) {

            this.actualSubscriber.next(t, v -> LiClassUtil.isAssignableFromOrIsWrapper(middleType, v.getClass()));
        }

    }

}
