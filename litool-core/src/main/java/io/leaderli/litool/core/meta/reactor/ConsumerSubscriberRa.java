package io.leaderli.litool.core.meta.reactor;

import io.leaderli.litool.core.meta.Lino;

import java.util.function.Consumer;

/**
 * 尾结点消费者
 *
 * @author leaderli
 * @since 2022/6/27
 */
public class ConsumerSubscriberRa<T> implements SubscriberRa<T> {
    private final Consumer<? super Lino<T>> consumer;

    public ConsumerSubscriberRa(Consumer<? super Lino<T>> consumer) {
        if (consumer == null) {
            consumer = (Consumer<Lino<T>>) lino -> {

            };
        }
        this.consumer = consumer;
    }

    @Override
    public void onSubscribe(SubscriptionRa prevSubscription) {
        prevSubscription.request(-1);

    }

    @Override
    public void next(Lino<T> t) {
        consumer.accept(t);
    }


    @Override
    public void onComplete() {

    }
}
