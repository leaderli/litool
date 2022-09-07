package io.leaderli.litool.core.meta.link;

import io.leaderli.litool.core.meta.Lino;

/**
 * @author leaderli
 * @since 2022/7/16
 */
public class CancelRunnable<T> extends Some<T, T> {

    private final Runnable runnable;


    public CancelRunnable(Publisher<T> prevPublisher, final Runnable runnable) {
        super(prevPublisher);
        this.runnable = runnable;
    }


    @Override
    public void subscribe(Subscriber<T> actualSubscriber) {
        prevPublisher.subscribe(new CancelRunnableSubscriber(actualSubscriber));
    }


    private class CancelRunnableSubscriber extends SameTypeIntermediateSubscriber<T> implements OnErrorSubscriber {


        protected CancelRunnableSubscriber(Subscriber<T> actualSubscriber) {
            super(actualSubscriber);
        }


        @Override
        public void next(T value) {

            this.actualSubscriber.next(value);
        }

        @Override
        public void onError(Lino<T> lino) {
            runnable.run();

            if (this.actualSubscriber instanceof OnErrorSubscriber) {
                this.actualSubscriber.onError(lino);

            }
        }
    }
}
