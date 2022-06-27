package io.leaderli.litool.core.meta;

import java.util.function.Consumer;

/**
 * @author leaderli
 * @since 2022/6/27
 */
public class ConsumerRaSubscriber<T> implements RaSubscriber<T> {
    private final Consumer<? super Lino<T>> consumer;

    public ConsumerRaSubscriber(Consumer<? super Lino<T>> consumer) {
        if(consumer==null){
            consumer = (Consumer<Lino<T>>) lino -> {

            };
        }
        this.consumer = consumer;
    }

    @Override
    public void onSubscribe(RaSubscription prevSubscription) {
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
