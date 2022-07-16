package io.leaderli.litool.core.meta.reactor;

import io.leaderli.litool.core.meta.Lino;

/**
 * @author leaderli
 * @since 2022/7/16
 */
class SomeSubscriptionLink<T> implements SubscriptionLink<T> {

    private final SubscriberLink<? super T> actualSubscriber;
    private final T value;

    public SomeSubscriptionLink(SubscriberLink<? super T> actualSubscriber, T value) {
        this.actualSubscriber = actualSubscriber;
        this.value = value;
    }


    @Override
    public void request() {

        Lino.of(value).ifPresent(actualSubscriber::next)
                .ifAbsent(() -> actualSubscriber.onCancel(Lino.none()));

    }
}
