package io.leaderli.litool.core.meta.reactor;

import io.leaderli.litool.core.meta.LiLink;

/**
 * @author leaderli
 * @since 2022/7/16
 */
public class SomeLink<T> extends LiLink<T> {


    private final T value;

    public SomeLink(T t) {
        this.value = t;

    }

    @Override
    public void subscribe(SubscriberLink<T> actualSubscriber) {
        SubscriptionLink<T> subscription = new SomeSubscriptionLink<>(actualSubscriber, value);
        actualSubscriber.onSubscribe(subscription);

    }
}
