package io.leaderli.litool.core.meta.link;

/**
 * @author leaderli
 * @since 2022/7/16
 */
class OnInterruptRunnableLink<T> extends SomeLink<T, T> {

    private final Runnable runnable;


    public OnInterruptRunnableLink(PublisherLink<T> prevPublisher, final Runnable runnable) {
        super(prevPublisher);
        this.runnable = runnable;
    }


    @Override
    public void subscribe(SubscriberLink<T> actualSubscriber) {
        prevPublisher.subscribe(new CancelRunnableSubscriber(actualSubscriber));
    }


    private class CancelRunnableSubscriber extends SameTypeIntermediateSubscriber<T> implements OnErrorSubscriber {


        protected CancelRunnableSubscriber(SubscriberLink<T> actualSubscriber) {
            super(actualSubscriber);
        }


        @Override
        public void next(T value) {

            this.actualSubscriber.next(value);
        }


        @Override
        public void onInterrupt(T value) {
            runnable.run();
            if (this.actualSubscriber instanceof OnErrorSubscriber) {
                this.actualSubscriber.onInterrupt(value);

            }
        }
    }
}
