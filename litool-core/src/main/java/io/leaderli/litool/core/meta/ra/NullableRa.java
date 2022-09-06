package io.leaderli.litool.core.meta.ra;

import java.util.function.Supplier;

/**
 * @author leaderli
 * @since 2022/8/30 7:25 PM
 */
class NullableRa<T> extends PublisherRa<T> {
    private final Supplier<? extends T> supplier;

    public NullableRa(Publisher<T> prevPublisher, Supplier<? extends T> supplier) {
        super(prevPublisher);
        this.supplier = supplier;
    }

    @Override
    public void subscribe(Subscriber<? super T> actualSubscriber) {
        prevPublisher.subscribe(new NullableSubscriberSubscription(actualSubscriber));

    }

    private class NullableSubscriberSubscription extends IntermediateSubscriberSubscription<T, T> {
        public NullableSubscriberSubscription(Subscriber<? super T> actualSubscriber) {
            super(actualSubscriber);
        }

        @Override
        public void next(T t) {
            this.actualSubscriber.next(t);
        }

        @Override
        public void next_null() {
            SubscriberUtil.next(actualSubscriber, supplier.get());
        }
    }
}
