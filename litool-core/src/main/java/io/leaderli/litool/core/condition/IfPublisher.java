package io.leaderli.litool.core.condition;

/**
 * 发布者
 */
public interface IfPublisher<T,R> {
    /**
     * 订阅者在发布者上进行订阅操作, 发布者在得到订阅信息后，一般会依次将向前进行订阅，
     * 最终达到首个节点，则会去调用{@link IfSubscriber#onSubscribe(IfSubscription)}
     *
     * @see IfSubscriber#onSubscribe(IfSubscription)
     */
    void subscribe(IfSubscriber<T, R> subscriber);
}
