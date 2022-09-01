package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.bit.BitPermission;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static io.leaderli.litool.core.meta.ra.LiraStatus.ARRIVED;
import static io.leaderli.litool.core.meta.ra.LiraStatus.COMPLETE;

class IterableSubscriber<T> implements Subscriber<T>, Iterator<T> {
    private final BitPermission iteratorState = new BitPermission();
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
        iteratorState.enable(LiraStatus.ARRIVED);
    }

    @Override
    public void onNull() {
        next = null;
        iteratorState.enable(LiraStatus.ARRIVED);
    }

    @Override
    public void onComplete() {
        request = false;
        iteratorState.enable(COMPLETE);
    }

    @Override
    public void onCancel() {
        iteratorState.enable(COMPLETE);
    }


    @Override
    public boolean hasNext() {

        if (iteratorState.have(ARRIVED)) {
            return true;
        }
        if (iteratorState.have(COMPLETE)) {
            return false;
        }
        // trigger


        while (iteratorState.miss(ARRIVED | COMPLETE)) {
            this.prevSubscription.request();
        }

        return iteratorState.have(ARRIVED);
    }

    @Override
    public T next() {

        if (hasNext()) {
            iteratorState.disable(ARRIVED);
            return next;
        }
        throw new NoSuchElementException();
    }


}
