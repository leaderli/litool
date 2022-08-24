package io.leaderli.litool.core.meta.ra;

import java.util.function.BiConsumer;

/**
 * 带有消费操作的尾结点消费者，可提前取消遍历操作，{@link CancelSubscriptionRa#cancel()}
 *
 * @author leaderli
 * @since 2022/6/27
 */
public class BiConsumerSubscriberRa<T> implements SubscriberRa<T> {
    /**
     * 该消费者仅保留 {@link CancelSubscriptionRa#cancel()} 操作，不允许使用 {@link SubscriptionRa#request()}
     */
    private final BiConsumer<? super T, CancelSubscriptionRa> consumer;
    private SubscriptionRa prevSubscription;

    public BiConsumerSubscriberRa(BiConsumer<? super T, CancelSubscriptionRa> consumer) {
        if (consumer == null) {
            consumer = (BiConsumer<T, CancelSubscriptionRa>) (lino, raSubscription) -> {

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
    public void next(T t) {
        consumer.accept(t, this.prevSubscription);
    }


    @Override
    public void onComplete() {

    }
}
