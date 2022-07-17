package io.leaderli.litool.core.meta.condition;

import java.util.function.Function;

/**
 * 订阅者
 */
public interface SubscriberIf<T, R> {

    /**
     * 当订阅动作发起时调用，通常情况下 onSubscribe 是通知链上的下一个节点 onSubscribe ，最终到链的尾
     * 部，链式调用最尾端的订阅者订阅时，将向前一个节点发送一个{@link SubscriptionIf#request(java.util.function.Consumer)} ()}请
     * 求，一直向前传递，直到数据来源的发布者接收到来自链上的数据请求，然后调用 {@link #next(Object, Function)}，
     * 链上的每个节点一般依次调用 {@link #next(Object, Function)} 最终会调用订阅者的 {@link #next(Object, Function)}
     *
     * @param subscription 订阅者事件处理函数
     */
    void onSubscribe(SubscriptionIf<R> subscription);

    /**
     * 发布者推送了一个消息
     *
     * @param t         源数据
     * @param predicate 断言函数
     */
    void next(T t, Function<? super T, ?> predicate);


}
