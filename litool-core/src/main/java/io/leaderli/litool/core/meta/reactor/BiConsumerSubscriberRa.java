package io.leaderli.litool.core.meta.reactor;

import io.leaderli.litool.core.meta.Lino;

import java.util.function.BiConsumer;

/**
 * 带有消费操作的尾结点消费者，可提前取消遍历操作，{@link CancelSubscriptionRa#cancel()}
 *
 * @author leaderli
 * @since 2022/6/27
 */
public class BiConsumerSubscriberRa<T> implements SubscriberRa<T> {
    private SubscriptionRa prevSubscription;
    /**
     * 该消费者仅保留 {@link CancelSubscriptionRa#cancel()} 操作，不允许使用 {@link SubscriptionRa#request()}
     */
    private final BiConsumer<? super Lino<T>, CancelSubscriptionRa> consumer;

    public BiConsumerSubscriberRa(BiConsumer<? super Lino<T>, CancelSubscriptionRa> consumer) {
        if (consumer == null) {
            consumer = (BiConsumer<Lino<T>, CancelSubscriptionRa>) (lino, raSubscription) -> {

            };
        }
        this.consumer = consumer;
    }

    @Override
    public void onSubscribe(SubscriptionRa prevSubscription) {
        this.prevSubscription = prevSubscription;
        this.prevSubscription.request();

    }

    @Override
    public void next(Lino<? extends T> t) {
        consumer.accept(Lino.narrow(t), this.prevSubscription);
    }


    @Override
    public void onComplete() {

    }
}
