package io.leaderli.litool.core.meta.condition;

import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/7/17
 * <p>
 * 后接 then 系列操作
 */
class IfThen<T, R> implements LiThen<T, R> {
    private final Publisher<T, R> prevPublisher;
    private final Function<? super T, ?> filter;

    public IfThen(Publisher<T, R> prevPublisher, Function<? super T, ?> filter) {
        this.prevPublisher = prevPublisher;
        this.filter = filter;
    }

    @Override
    public void subscribe(Subscriber<T, R> actualSubscriber) {
        prevPublisher.subscribe(new SubscriberThen(actualSubscriber));

    }

    private class SubscriberThen extends IntermediateSubscriber<T, R> {

        public SubscriberThen(Subscriber<T, R> actualSubscriber) {
            super(actualSubscriber);

        }


        @Override
        public void next(T t, Function<? super T, ?> predicate) {

            this.actualSubscriber.next(t, filter);
        }

    }
}
