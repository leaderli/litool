package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.meta.Either;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author leaderli
 * @since 2022/9/29
 */
public class EitherRa<L, T> extends Ra<Either<L, T>> {

    private final PublisherRa<T> prevPublisher;
    private final Supplier<? extends L> left;

    public EitherRa(PublisherRa<T> prevPublisher, Supplier<? extends L> left) {
        Objects.requireNonNull(left);
        this.prevPublisher = prevPublisher;
        this.left = left;
    }

    @Override
    public void subscribe(SubscriberRa<? super Either<L, T>> actualSubscriber) {
        prevPublisher.subscribe(new EitherSubscriberSubscription(actualSubscriber));

    }

    private class EitherSubscriberSubscription extends IntermediateSubscriberSubscription<T, Either<L, T>> {


        public EitherSubscriberSubscription(SubscriberRa<? super Either<L, T>> actualSubscriber) {
            super(actualSubscriber);
        }

        @Override
        public void next(T t) {
            actualSubscriber.next(Either.right(t));
        }

        @Override
        public void next_null() {
            actualSubscriber.next(Either.left(left.get()));
        }

    }

}
