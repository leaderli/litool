package io.leaderli.litool.core.meta.logic;

/**
 * @author leaderli
 * @since 2022/9/12
 */
public interface SubscriberLogic<T> {

    void onSubscribe(SubscriptionLogic<T> subscription);

    /**
     * @param t    the test value
     * @param last the last test result
     */
    void next(T t, boolean last);

    /**
     * only call on {@link  NotSome}, and notified at {@link  TestSome}
     */
    void onNot();

    /**
     * sometimes it could be foreknowledge the result such as  or last is {@code  true},
     * it's can complete early
     *
     * @param result the complete result
     */
    void onComplete(boolean result);

}
