package io.leaderli.litool.core.meta.condition;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author leaderli
 * @since 2022/7/17
 */
class OtherPublisher<T, R> implements Publisher<T, R> {
    private final Publisher<T, R> prevPublisher;
    private final Supplier<? extends R> supplier;

    public OtherPublisher(Publisher<T, R> prevPublisher, Supplier<? extends R> supplier) {
        this.prevPublisher = prevPublisher;
        this.supplier = supplier;
    }

    @Override
    public void subscribe(Subscriber<T, R> actualSubscriber) {
        prevPublisher.subscribe(new SubscriberOtherThen<>(supplier, actualSubscriber));

    }

    private static class SubscriberOtherThen<T, R> extends IntermediateSubscriber<T, R> {
        private final Supplier<? extends R> supplier;

        public SubscriberOtherThen(Supplier<? extends R> supplier, Subscriber<T, R> actualSubscriber) {
            super(actualSubscriber);
            this.supplier = supplier;

        }


        @Override
        public void next(T t, Function<? super T, ?> predicate) {
            if (supplier != null) {
                onComplete(supplier.get());
            }
        }

    }
}
