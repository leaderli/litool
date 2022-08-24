package io.leaderli.litool.core.meta.ra;

import java.util.function.Consumer;

/**
 * 尾结点消费者
 *
 * @author leaderli
 * @since 2022/6/27
 */
public class ConsumerSubscriberRa<T> implements SubscriberRa<T> {
    private final Consumer<? super T> consumer;

    public ConsumerSubscriberRa(Consumer<? super T> consumer) {
        if (consumer == null) {
            consumer = lino -> {
            };
        }
        this.consumer = consumer;
    }

    @Override
    public void onSubscribe(SubscriptionRa prevSubscription) {
        prevSubscription.request();

    }

    @Override
    public void next(T t) {
        consumer.accept(t);
    }


    @Override
    public void onComplete() {

    }
}
