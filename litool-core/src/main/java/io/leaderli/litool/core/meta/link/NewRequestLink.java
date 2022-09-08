package io.leaderli.litool.core.meta.link;

/**
 * @author leaderli
 * @since 2022/7/16
 */
class NewRequestLink<T> extends SomeLink<T, T> {


    private final T newValue;

    public NewRequestLink(PublisherLink<T> prevPublisher, T newValue) {
        super(prevPublisher);
        this.newValue = newValue;
    }


    @Override
    public void subscribe(SubscriberLink<T> actualSubscriber) {
        prevPublisher.subscribe(new NewRequestSubscriber(actualSubscriber));

    }

    /**
     * @author leaderli
     * @since 2022/7/16
     */
    private class NewRequestSubscriber extends SameTypeIntermediateSubscriber<T> {

        public NewRequestSubscriber(SubscriberLink<T> actualSubscriber) {
            super(actualSubscriber);
        }

        @Override
        public void request() {
            this.prevSubscription.request(newValue);
        }

        @Override
        public void next(T value) {
            this.actualSubscriber.next(value);
        }
    }
}
