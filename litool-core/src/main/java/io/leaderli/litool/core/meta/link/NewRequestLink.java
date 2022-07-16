package io.leaderli.litool.core.meta.link;

import io.leaderli.litool.core.meta.SomeLink;

/**
 * @author leaderli
 * @since 2022/7/16
 */
public class NewRequestLink<T> extends SomeLink<T> {


    private final T newValue;

    public NewRequestLink(PublisherLink<T> prevPublisher, T newValue) {
        super(prevPublisher);
        this.newValue = newValue;
    }


    @Override
    public void subscribe(SubscriberLink<T> actualSubscriber) {
        prevPublisher.subscribe(new NewRequestSubscriberLink(actualSubscriber));

    }

    /**
     * @author leaderli
     * @since 2022/7/16
     */
    private class NewRequestSubscriberLink extends IntermediateSubscriberLink<T> {

        public NewRequestSubscriberLink(SubscriberLink<T> actualSubscriber) {
            super(actualSubscriber);
        }

        @Override
        public void request() {
            this.prevSubscription.request(newValue);
        }

        @Override
        public void next(T value) {
            this.actualSubscriber.next(newValue);
        }
    }
}
