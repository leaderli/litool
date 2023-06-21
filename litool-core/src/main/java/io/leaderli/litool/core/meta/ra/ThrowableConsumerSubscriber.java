package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.function.ThrowableConsumer;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * tail node consumer
 *
 * @author leaderli
 * @since 2022/6/27
 */
class ThrowableConsumerSubscriber<T> implements SubscriberRa<T> {
    private final ThrowableConsumer<? super T> consumer;
    private final Consumer<Throwable> whenThrow;
    private boolean done;


    public ThrowableConsumerSubscriber(ThrowableConsumer<? super T> consumer, Consumer<Throwable> whenThrow) {
        this.whenThrow = whenThrow;
        Objects.requireNonNull(consumer);
        this.consumer = consumer;
    }

    @Override
    public void onSubscribe(SubscriptionRa prevSubscription) {
        //  terminal
        while (!done) {
            prevSubscription.request();
        }
    }

    @Override
    public void next(T t) {
        try {
            consumer.accept(t);
        } catch (Throwable e) {
            whenThrow.accept(e);
        }
    }

    @Override
    public void onComplete() {
        done = true;
    }
}
