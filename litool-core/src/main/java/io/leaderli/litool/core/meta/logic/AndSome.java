package io.leaderli.litool.core.meta.logic;

/**
 * 逻辑运算类，表示多个订阅者同时满足条件时，所有订阅者都会收到数据。
 * 继承自 LogicSome 抽象类。
 *
 * @param <T> 订阅者订阅的数据类型。
 */
class AndSome<T> extends LogicSome<T> {

    public AndSome(Publisher<T> prePublisher) {
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
                super.next(t, lastState);
            } else {
                this.actualSubscriber.onComplete(false);
            }
        }
    }


}
