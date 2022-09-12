package io.leaderli.litool.core.meta.link;

/**
 * @author leaderli
 * @since 2022/7/16
 */
public class ValueLink<T> extends SomeLink<T, T> {


    private final T value;

    public ValueLink(T t) {
        super(null);
        this.value = t;

    }

    @Override
    public void subscribe(SubscriberLink<T> actualSubscriber) {
        actualSubscriber.onSubscribe(new ValueSubscription(actualSubscriber));
    }

    /**
     * @author leaderli
     * @since 2022/7/16
     */
    private class ValueSubscription implements SubscriptionLink {

        private final SubscriberLink<? super T> actualSubscriber;

        public ValueSubscription(SubscriberLink<? super T> actualSubscriber) {
            this.actualSubscriber = actualSubscriber;
        }


        @Override
        public void request() {

            if (value != null) {

                this.actualSubscriber.next(value);
            } else {
                this.actualSubscriber.onInterrupt(null);
            }
        }
    }
}
