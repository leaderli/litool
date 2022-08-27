package io.leaderli.litool.core.meta.condition;

/**
 * 发布者
 */
public interface PublisherIf<T, R> {
/**
 * 订阅者在发布者上进行订阅操作, 发布者在得到订阅信息后，一般会依次将向前进行订阅，
 * 最终达到首个节点，则会去调用{@link SubscriberIf#onSubscribe(SubscriptionIf)}
 *
 * @param subscriber 订阅者
 * @see SubscriberIf#onSubscribe(SubscriptionIf)
 */
void subscribe(SubscriberIf<T, R> subscriber);
}
