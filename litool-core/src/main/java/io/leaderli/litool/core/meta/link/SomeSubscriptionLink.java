package io.leaderli.litool.core.meta.link;

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

        request(value);

    }

    @Override
    public void request(T t) {

        Lino.of(t).ifPresent(actualSubscriber::next)
                .ifAbsent(() -> actualSubscriber.onCancel(Lino.none()));
    }
}
