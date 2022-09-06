package io.leaderli.litool.core.meta.condition;

import io.leaderli.litool.core.type.ClassUtil;

/**
 * @author leaderli
 * @since 2022/7/17
 */
class InstanceOfThen<T, M, R> implements LiInstanceOfThen<T, M, R> {
    private final Publisher<T, R> prevPublisher;
    private final Class<? extends M> middleType;

    public InstanceOfThen(Publisher<T, R> prevPublisher, Class<? extends M> middleType) {
        this.prevPublisher = prevPublisher;
        this.middleType = middleType;
    }

    @Override
    public void subscribe(Subscriber<T, R> actualSubscriber) {
        this.prevPublisher.subscribe(new SubscriberInstanceOfThen(actualSubscriber));

    }

    private class SubscriberInstanceOfThen extends IntermediateSubscriber<T, R> {

        public SubscriberInstanceOfThen(Subscriber<T, R> actualSubscriber) {
            super(actualSubscriber);
        }


        @Override
        public void next(T t) {

            if (ClassUtil.isAssignableFromOrIsWrapper(InstanceOfThen.this.middleType, t.getClass())) {
                this.actualSubscriber.apply(t);
            } else {
                this.actualSubscriber.next(t);
            }
        }


    }

}
