package io.leaderli.litool.core.meta.condition;

import io.leaderli.litool.core.type.LiClassUtil;

import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/17
 */
public
class IfInstanceOfThen<T, M, R> implements LiInstanceOfThen<T, M, R> {
    private final PublisherIf<T, R> prevPublisher;
    private final Class<? extends M> middleType;

    public IfInstanceOfThen(PublisherIf<T, R> prevPublisher, Class<? extends M> middleType) {
        this.prevPublisher = prevPublisher;
        this.middleType = middleType;
    }

    @Override
    public void subscribe(SubscriberIf<T, R> actualSubscriber) {
        prevPublisher.subscribe(new SubscriberIfInstanceOfThen(actualSubscriber));

    }

    private class SubscriberIfInstanceOfThen extends IntermediateSubscriberIf<T, R> {

        public SubscriberIfInstanceOfThen(SubscriberIf<T, R> actualSubscriber) {
            super(actualSubscriber);

        }


        @Override
        public void next(T t, Function<? super T, ?> predicate) {

            this.actualSubscriber.next(t, v -> LiClassUtil.isAssignableFromOrIsWrapper(middleType, v.getClass()));
        }

    }

}
