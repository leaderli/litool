package io.leaderli.litool.core.condition;

import io.leaderli.litool.core.meta.LiBox;

import java.util.function.Function;

/**
 * 订阅者
 */
public interface IfSubscriber<T, R> {

    /**
     * 当订阅动作发起时调用，通常情况下 onSubscribe 是通知链上的下一个节点 onSubscribe ，最终到链的尾
     * 部，链式调用最尾端的订阅者订阅时，将向前一个节点发送一个{@link IfSubscription#request(LiBox)} ()}请
     * 求，一直向前传递，直到数据来源的发布者接收到来自链上的数据请求，然后调用 {@link #next(Object, Function)}，
     * 链上的每个节点一般依次调用 {@link #(Object, Function)} 最终会调用订阅者的 {@link #next(Object, Function)}
     */
    void onSubscribe(IfSubscription<R> subscription);

    /**
     * 发布者推送了一个消息
     */
    void next(T t, Function<T, Object> predicate);


}
