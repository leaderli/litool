package io.leaderli.litool.core.meta.link;

import io.leaderli.litool.core.meta.SomeLink;

/**
 * @author leaderli
 * @since 2022/7/16
 */
public class ValueLink<T> extends SomeLink<T> {


    private final T value;

    public ValueLink(T t) {
        this.value = t;

    }

    @Override
    public void subscribe(SubscriberLink<T> actualSubscriber) {
        SubscriptionLink<T> subscription = new SomeSubscriptionLink<>(actualSubscriber, value);
        actualSubscriber.onSubscribe(subscription);
    }
}
