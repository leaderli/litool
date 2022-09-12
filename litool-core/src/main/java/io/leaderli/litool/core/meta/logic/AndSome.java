package io.leaderli.litool.core.meta.logic;

/**
 * @author leaderli
 * @since 2022/9/12
 */
class AndSome<T> extends LogicSome<T> {

    public AndSome(PublisherLogic<T> prePublisher) {
        super(prePublisher);
    }

    @Override
    public void subscribe(SubscriberLogic<T> actualSubscriber) {
        prevPublisher.subscribe(new TestSubscriberSubscription(actualSubscriber));

    }

    private class TestSubscriberSubscription extends IntermediateSubscriberSubscription<T> {


        protected TestSubscriberSubscription(SubscriberLogic<T> actualSubscriber) {
            super(actualSubscriber);
        }

        @Override
        public void next(T t, boolean last) {

            if (last) {
                super.next(t, last);
            } else {
                this.actualSubscriber.onComplete(false);
            }
        }
    }


}
