package io.leaderli.litool.core.meta.ra;

import java.util.function.Consumer;

/**
 * 尾结点消费者
 *
 * @author leaderli
 * @since 2022/6/27
 */
public class ConsumerSubscriber<T> implements Subscriber<T> {
    private final Consumer<? super T> consumer;


    public ConsumerSubscriber(Consumer<? super T> consumer) {
        if (consumer == null) {
            consumer = lino -> {
            };
        }
        this.consumer = consumer;
    }

    @Override
    public void onSubscribe(Subscription prevSubscription) {
        //  terminal
        prevSubscription.request();
    }

    @Override
    public void next(T t) {
        consumer.accept(t);
    }


}
