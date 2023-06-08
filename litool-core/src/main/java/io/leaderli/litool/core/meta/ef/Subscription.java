package io.leaderli.litool.core.meta.ef;

/**
 * 表示一个{@link Subscriber}订阅{@link Publisher}的一对一生命周期的{@link Subscription}。
 * <p>
 * 它只能被单个{@link Subscriber}使用一次。
 *
 * @see Subscriber
 * @see Publisher
 */
@FunctionalInterface
public interface Subscription {

    /**
     * 通过调用此方法，{@link Publisher}才会发送事件。
     */
    void request();

}
