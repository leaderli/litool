package io.leaderli.litool.core.meta.logic;

/**
 * @author leaderli
 * @since 2022/9/12
 */
class BeginSome<T> extends LogicSome<T> {
    protected BeginSome() {
        super(null);
    }


    @Override
    public void subscribe(SubscriberLogic<T> actualSubscriber) {
        actualSubscriber.onSubscribe(new BeginSubscriber(actualSubscriber));
    }

    private class BeginSubscriber implements SubscriberLogic<T>, SubscriptionLogic<T> {
        protected final SubscriberLogic<T> actualSubscriber;


        protected BeginSubscriber(SubscriberLogic<T> actualSubscriber) {
            this.actualSubscriber = actualSubscriber;
        }

        @Override
        public final void onSubscribe(SubscriptionLogic<T> prevSubscription) {
            actualSubscriber.onSubscribe(this);
        }

        @Override
        public void next(T t, boolean last) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void onNot() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void onComplete(boolean result) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void request(T value) {
            this.actualSubscriber.next(value, true);
        }

    }

}
