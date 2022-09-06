package io.leaderli.litool.core.meta.condition;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/9/6
 */
class End<T, R> implements Subscriber<T, R> {

    private final Consumer<? super R> completeConsumer;

    End(Consumer<? super R> completeConsumer) {
        this.completeConsumer = completeConsumer;
    }

    @Override
    public void onSubscribe(Subscription<R> subscription) {

        subscription.request(completeConsumer);
    }

    @Override
    public void next(T t, Function<? super T, ?> predicate) {

        throw new UnsupportedOperationException();
    }

    public void request(OtherPublisher<T, R> otherPublisher) {
        otherPublisher.subscribe(this);
    }
}
