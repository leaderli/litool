package io.leaderli.litool.core.meta;

import java.util.function.BiConsumer;

/**
 * 带有消费操作的尾结点消费者，可提前取消遍历操作，{@link RaCancelSubscription#cancel()}
 *
 * @author leaderli
 * @since 2022/6/27
 */
public class BiConsumerRaSubscriber<T> implements RaSubscriber<T> {
    private RaSubscription prevSubscription;
    /**
     * 该消费者仅保留 {@link RaCancelSubscription#cancel()} 操作，不允许使用 {@link RaSubscription#request(int)}
     */
    private final BiConsumer<? super Lino<T>, RaCancelSubscription> consumer;

    public BiConsumerRaSubscriber(BiConsumer<? super Lino<T>, RaCancelSubscription> consumer) {
        if (consumer == null) {
            consumer = (BiConsumer<Lino<T>, RaCancelSubscription>) (lino, raSubscription) -> {

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
