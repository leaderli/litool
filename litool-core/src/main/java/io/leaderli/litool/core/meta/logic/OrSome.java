package io.leaderli.litool.core.meta.logic;

/**
 * @author leaderli
 * @since 2022/9/12
 */
class OrSome<T> extends LogicSome<T> {

    public OrSome(PublisherLogic<T> prePublisher) {
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
                this.actualSubscriber.onComplete(true);
            } else {
                super.next(t, last);
            }
        }
    }


}
