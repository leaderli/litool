package io.leaderli.litool.core.meta.ra;


import java.util.Iterator;

/**
 * @author leaderli
 * @since 2022/7/16
 */
public final class MediateSubscription<R> implements Subscription {

    private final Completable mediate;
    private final Subscriber<? super R> actualSubscriber;

    private final Iterator<? extends R> iterator;

    private boolean canceled;


    public MediateSubscription(Completable mediate, Subscriber<? super R> actualSubscriber, Iterator<? extends R> iterator) {
        this.mediate = mediate;
        this.actualSubscriber = actualSubscriber;
        this.iterator = iterator;
    }


    @SuppressWarnings("java:S2583")
    @Override
    public void request() {

        if (canceled) {
            return;
        }

        if (iterator.hasNext()) {
            actualSubscriber.beforeRequest();
            if (canceled) {
                return;
            }

            try {

                SubscriberUtil.next(actualSubscriber, iterator.next());
            } catch (Throwable throwable) {
                actualSubscriber.onError(throwable, this);
                actualSubscriber.onNull();
            }
            actualSubscriber.onRequested();

        } else {
            mediate.onComplete();
        }


    }


    @Override
    public Subscription prevSubscription() {
        return this;
    }

    @Override
    public void cancel() {
        canceled = true;
        actualSubscriber.onCancel();
    }

}





