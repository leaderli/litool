package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.bit.BitPermission;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static io.leaderli.litool.core.meta.ra.IteratorStatus.ARRIVED;
import static io.leaderli.litool.core.meta.ra.IteratorStatus.COMPLETE;

/**
 * @author leaderli
 * @since 2022/8/31 11:05 AM
 */
class IteratorStatus {

    /**
     * request a element from {@link  ArraySome}
     */
    public static final int REQUEST = 0b1;
    /**
     * a signal have reached the  {@link  Subscriber}
     */
    public static final int ARRIVED = 0b10;

    /**
     * lira has execute to the end
     */
    public static final int COMPLETE = 0b1000;
}

class IterableSubscriber<T> implements Subscriber<T>, Iterator<T> {
    private final BitPermission<IteratorStatus> iteratorState = new BitPermission<>();
    boolean request = true;
    private Subscription prevSubscription;
    private T next;

    @Override
    public void onSubscribe(Subscription prevSubscription) {
        this.prevSubscription = prevSubscription.prevSubscription();
    }

    @Override
    public void next(T t) {

        next = t;
        iteratorState.enable(IteratorStatus.ARRIVED);
    }

    @Override
    public void onNull() {
        next = null;
        iteratorState.enable(IteratorStatus.ARRIVED);
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

//    @SuppressWarnings("java:S4348")
//    @Override
//    public Iterator<T> iterator() {
//        return this;
//    }

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
