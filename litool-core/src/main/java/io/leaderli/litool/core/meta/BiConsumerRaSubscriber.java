package io.leaderli.litool.core.meta;

import java.util.function.BiConsumer;

/**
 * @author leaderli
 * @since 2022/6/27
 */
public class BiConsumerRaSubscriber<T> implements RaSubscriber<T> {
    private RaSubscription prevSubscription;
    private final BiConsumer<? super Lino<T>, RaSubscription> consumer;

    public BiConsumerRaSubscriber(BiConsumer<? super Lino<T>, RaSubscription> consumer) {
        if (consumer == null) {
            consumer = (BiConsumer<Lino<T>, RaSubscription>) (lino, raSubscription) -> {

            };
        }
        this.consumer = consumer;
    }

    @Override
    public void onSubscribe(RaSubscription prevSubscription) {
        this.prevSubscription = prevSubscription;
        this.prevSubscription.request(-1);

    }

    @Override
    public void next(Lino<T> t) {
        consumer.accept(t, this.prevSubscription);
    }


    @Override
    public void onComplete() {

    }
}
