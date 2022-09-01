package io.leaderli.litool.core.meta.ra;


import java.util.Iterator;

/**
 * @author leaderli
 * @since 2022/7/16
 */
public final class FlatArraySome<R> implements Subscription {

    private final Completable completable;
    private final Subscriber<? super R> actualSubscriber;

    private final Iterator<? extends R> iterator;

    private boolean canceled;


    public FlatArraySome(Completable completable, Subscriber<? super R> actualSubscriber, Iterator<? extends R> iterator) {
        this.completable = completable;
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
            R t = iterator.next();


            try {

                SubscriberUtil.next(actualSubscriber, t);
            } catch (Throwable throwable) {
                actualSubscriber.onError(throwable, this);
                actualSubscriber.onNull();
            }
            actualSubscriber.onRequested();

        } else {
            completable.onComplete();
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





