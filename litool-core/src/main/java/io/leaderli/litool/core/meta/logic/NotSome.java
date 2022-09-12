package io.leaderli.litool.core.meta.logic;

/**
 * @author leaderli
 * @since 2022/9/12
 */
class NotSome<T> extends LogicSome<T> {
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
