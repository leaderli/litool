package io.leaderli.litool.core.stream;

/**
 * @author leaderli
 * @since 2022/9/12
 */
public interface SubscriberLogic<T> {

    void onSubscribe(SubscriptionLogic<T> subscription);

    void next(T t, boolean last);

    void onNot();

    void onComplete(boolean result);

}
