package io.leaderli.litool.core.meta.ra;

import java.util.Iterator;
import java.util.NoSuchElementException;

class IterableSubscriber<T> implements SubscriberRa<T>, Iterator<T> {
    private SubscriptionRa prevSubscription;
    private T next;
    private boolean accepted;
    private boolean completed;

    @Override
    public void onSubscribe(SubscriptionRa prevSubscription) {
        this.prevSubscription = prevSubscription;
    }

    @Override
    public void next(T t) {
        arrive(t);
    }

    private void arrive(T t) {
        next = t;
        accepted = true;
    }

    @Override
    public void next_null() {
        arrive(null);
    }

    @Override
    public void onComplete() {
        completed = true;
    }

    @Override
    public void onCancel() {
        completed = true;
    }


    @SuppressWarnings("java:S2583")
    @Override
    public boolean hasNext() {

        if (completed) {
            return false;
        }
        if (accepted) {
            return true;
        }

        // trigger
        while (true) {
            this.prevSubscription.request(LiraBit.ITR);
            if (completed) {
                return false;
            }
            if (accepted) {
                return true;
            }

        }

    }

    @Override
    public T next() {

        if (hasNext()) {
            accepted = false;
            return next;
        }
        throw new NoSuchElementException();
    }


}
