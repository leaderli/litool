package io.leaderli.litool.core.meta.logic;

/**
 * 订阅者逻辑接口，用于接收订阅事件的回调
 *
 * @param <T> 订阅事件对象的类型
 */
public interface Subscriber<T> {

    /**
     * 订阅事件回调方法，当发布者成功订阅时调用
     *
     * @param subscription 订阅事件对象
     */
    void onSubscribe(Subscription<T> subscription);

    /**
     * 订阅事件回调方法，当发布者推送新的订阅事件时调用
     *
     * @param t         订阅事件对象
     * @param lastState 最后的状态
     */
    void next(T t, boolean lastState);

    /**
     * 订阅事件回调方法，当发布者未推送任何订阅事件时调用
     * 仅在 {@link NotSome} 状态下调用
     */
    void onNot();

    /**
     * 订阅事件回调方法，当发布者推送的订阅事件已全部接收时调用
     *
     * @param result 订阅事件接收结果，{@code true} 表示全部接收成功，{@code false} 表示存在接收失败的订阅事件
     */
    void onComplete(boolean result);

}
