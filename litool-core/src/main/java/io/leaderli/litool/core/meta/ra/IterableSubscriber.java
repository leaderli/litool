package io.leaderli.litool.core.meta.ra;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static io.leaderli.litool.core.meta.ra.LiraBit.ARRIVED;
import static io.leaderli.litool.core.meta.ra.LiraBit.COMPLETE;

class IterableSubscriber<T> implements Subscriber<T>, Iterator<T> {
    private final LiraBit liraState = new LiraBit();
    boolean request = true;
    private Subscription prevSubscription;
    private T next;

    @Override
    public void onSubscribe(Subscription prevSubscription) {
        this.prevSubscription = prevSubscription;
    }

    @Override
    public void next(T t) {

        next = t;
        liraState.enable(ARRIVED);
    }

    @Override
    public void onNull() {
        next = null;
        liraState.enable(ARRIVED);
    }

    @Override
    public void onComplete() {
        request = false;
        liraState.enable(COMPLETE);
    }

    @Override
    public void onCancel() {
        liraState.enable(COMPLETE);
    }


    @Override
    public boolean hasNext() {

        if (liraState.have(ARRIVED)) {
            return true;
        }
        if (liraState.have(COMPLETE)) {
            return false;
        }
        // trigger


        while (liraState.miss(ARRIVED | COMPLETE)) {
            this.prevSubscription.request();
        }

        return liraState.have(ARRIVED);
    }

    @Override
    public T next() {

        if (hasNext()) {
            liraState.disable(ARRIVED);
            return next;
        }
        throw new NoSuchElementException();
    }


}
