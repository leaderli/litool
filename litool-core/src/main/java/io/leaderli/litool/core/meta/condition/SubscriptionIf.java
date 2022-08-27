package io.leaderli.litool.core.meta.condition;

import java.util.function.Consumer;

public interface SubscriptionIf<T> {

/**
 * 请求数据
 *
 * @param completeConsumer 调用链结束时调用的消费者
 */
void request(Consumer<? super T> completeConsumer);

/**
 * @param value 调用链结束时调用的最终结果
 */
void onComplete(T value);
}
