package io.leaderli.litool.core.meta.logic;

/**
 * 消息发布者，用于订阅消息。
 *
 * @param <T> 消息的类型
 */
@FunctionalInterface
public interface Publisher<T> {
    /**
     * 订阅一个消息处理器。
     *
     * @param subscriber 消息的订阅者
     */
    void subscribe(Subscriber<T> subscriber);

}
