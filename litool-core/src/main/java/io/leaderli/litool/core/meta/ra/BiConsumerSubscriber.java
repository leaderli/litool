package io.leaderli.litool.core.meta.ra;

import java.util.function.BiConsumer;

/**
 * 带有消费操作的尾结点消费者，可提前取消遍历操作，{@link CancelSubscription#cancel()}
 *
 * @author leaderli
 * @since 2022/6/27
 */
public class BiConsumerSubscriber<T> implements Subscriber<T> {
    /**
     * 该消费者仅保留 {@link CancelSubscription#cancel()} 操作，不允许使用 {@link Subscription#request()}
     */
    private final BiConsumer<? super T, CancelSubscription> consumer;
    private Subscription prevSubscription;

    public BiConsumerSubscriber(BiConsumer<? super T, CancelSubscription> consumer) {
        if (consumer == null) {
            consumer = (BiConsumer<T, CancelSubscription>) (lino, raSubscription) -> {

            };
        }
        this.consumer = consumer;
    }

    @Override
    public void onSubscribe(Subscription prevSubscription) {
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
