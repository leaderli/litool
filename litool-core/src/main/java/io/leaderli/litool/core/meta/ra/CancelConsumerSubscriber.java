package io.leaderli.litool.core.meta.ra;

import java.util.Objects;

/**
 * 带有消费操作的尾结点消费者，可提前取消遍历操作，{@link CancelSubscription#cancel()}
 *
 * @author leaderli
 * @since 2022/6/27
 */
class CancelConsumerSubscriber<T> implements Subscriber<T> {
    /**
     * 该消费者仅保留 {@link CancelSubscription#cancel()} 操作，不允许使用
     * {@link Subscription#request(int)}
     */
    private final CancelConsumer<? super T> consumer;
    private Subscription prevSubscription;

    public CancelConsumerSubscriber(CancelConsumer<? super T> consumer) {
        Objects.requireNonNull(consumer);
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


}
