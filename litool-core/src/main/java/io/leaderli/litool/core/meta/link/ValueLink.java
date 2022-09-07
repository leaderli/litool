package io.leaderli.litool.core.meta.link;

import io.leaderli.litool.core.meta.Lino;

/**
 * @author leaderli
 * @since 2022/7/16
 */
class ValueLink<T> extends SomeLink<T, T> {


    private final T value;

    public ValueLink(T t) {
        super(null);
        this.value = t;

    }

    @Override
    public void subscribe(Subscriber<T> actualSubscriber) {
        Subscription<T> subscription = new ValueSubscription(actualSubscriber);
        actualSubscriber.onSubscribe(subscription);
    }

    /**
     * @author leaderli
     * @since 2022/7/16
     */
    private class ValueSubscription implements Subscription<T> {

        private final Subscriber<? super T> actualSubscriber;

        public ValueSubscription(Subscriber<? super T> actualSubscriber) {
            this.actualSubscriber = actualSubscriber;
        }


        @Override
        public void request() {

            request(value);

        }

        @Override
        public void request(T t) {

            Lino.of(t).ifPresent(actualSubscriber::next)
                    .ifAbsent(() -> actualSubscriber.onError(Lino.none()));
        }
    }
}
