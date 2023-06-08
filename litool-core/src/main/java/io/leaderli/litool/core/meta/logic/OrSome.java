package io.leaderli.litool.core.meta.logic;

/**
 * 或操作类
 *
 * @author leaderli
 * @since 2022/9/12
 */
class OrSome<T> extends LogicSome<T> {

    public OrSome(Publisher<T> prePublisher) {
        super(prePublisher);
    }

    @Override
    public void subscribe(Subscriber<T> actualSubscriber) {
        prevPublisher.subscribe(new TestSubscriberSubscription(actualSubscriber));

    }

    private class TestSubscriberSubscription extends IntermediateSubscriberSubscription<T> {


        protected TestSubscriberSubscription(Subscriber<T> actualSubscriber) {
            super(actualSubscriber);
        }

        @Override
        public void next(T t, boolean lastState) {

            if (lastState) {
                this.actualSubscriber.onComplete(true);
            } else {
                super.next(t, lastState);
            }
        }
    }


}
