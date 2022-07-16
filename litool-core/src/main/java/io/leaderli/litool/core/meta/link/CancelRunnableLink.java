package io.leaderli.litool.core.meta.link;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.SomeLink;

/**
 * @author leaderli
 * @since 2022/7/16
 */
public class CancelRunnableLink<T> extends SomeLink<T> {

    private final Runnable runnable;


    public CancelRunnableLink(PublisherLink<T> prevPublisher, final Runnable runnable) {
        super(prevPublisher);
        this.runnable = runnable;
    }


    @Override
    public void subscribe(SubscriberLink<T> actualSubscriber) {
        prevPublisher.subscribe(new CancelRunnableSubscriberLink(actualSubscriber));
    }


    private class CancelRunnableSubscriberLink extends IntermediateSubscriberLink<T> implements ErrorLink {


        protected CancelRunnableSubscriberLink(SubscriberLink<T> actualSubscriber) {
            super(actualSubscriber);
        }


        @Override
        public void next(T value) {

            this.actualSubscriber.next(value);
        }

        @Override
        public void onCancel(Lino<T> lino) {
            runnable.run();

            if (this.actualSubscriber instanceof ErrorLink) {
                this.actualSubscriber.onCancel(lino);

            }
        }
    }
}
