package io.leaderli.litool.core.meta.ra;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * 尾结点消费者
 *
 * @author leaderli
 * @since 2022/6/27
 */
class ConsumerSubscriber<T> implements SubscriberRa<T> {
    private final Consumer<? super T> consumer;


    public ConsumerSubscriber(Consumer<? super T> consumer) {
        Objects.requireNonNull(consumer);
        this.consumer = consumer;
    }

    @Override
    public void onSubscribe(SubscriptionRa prevSubscription) {
        //  terminal
        prevSubscription.request();
    }

    @Override
    public void next(T t) {
        consumer.accept(t);
    }


}
