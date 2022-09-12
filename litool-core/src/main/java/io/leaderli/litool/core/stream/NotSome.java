package io.leaderli.litool.core.stream;

/**
 * @author leaderli
 * @since 2022/9/12
 */
public class NotSome<T> extends Some<T> {
    public NotSome(PublisherLogic<T> prePublisher) {
        super(prePublisher);
    }

    @Override
    public void subscribe(SubscriberLogic<T> actualSubscriber) {
        prevPublisher.subscribe(new NotSubscriberSubscription(actualSubscriber));

    }

    private class NotSubscriberSubscription extends IntermediateSubscriberSubscription<T> {


        protected NotSubscriberSubscription(SubscriberLogic<T> actualSubscriber) {
            super(actualSubscriber);
        }

        @Override
        public void next(T t, boolean last) {

            this.actualSubscriber.onNot();
            super.next(t, last);
        }
    }

}
