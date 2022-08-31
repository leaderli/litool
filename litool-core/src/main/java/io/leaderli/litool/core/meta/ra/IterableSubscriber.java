package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.bit.BitPermission;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

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

class IterableSubscriber<T> implements Subscriber<T>, Iterable<T>, Iterator<T> {
    private final BitPermission<IteratorStatus> iteratorState = new BitPermission<>();
    private final Queue<T> queue = new ArrayDeque<>();
    private Subscription prevSubscription;
//    private T next;

    @Override
    public void onSubscribe(Subscription prevSubscription) {
        this.prevSubscription = prevSubscription.prevSubscription();
    }


    @Override
    public void next(T t) {

        this.queue.add(t);
//        iteratorState.enable(ARRIVED);
    }

    @Override
    public void onNull() {
        this.queue.add(null);
//        iteratorState.enable(ARRIVED);
    }

    @Override
    public void onComplete() {
        iteratorState.enable(COMPLETE);
    }


    @Override
    public Iterator<T> iterator() {
//        System.out.println("init:" + iteratorState);
        return this;
    }

    @Override
    public boolean hasNext() {

        if (iteratorState.have(COMPLETE)) {
            return false;
        }
        // trigger

//        while (true) {
//            if (iteratorState.have(ARRIVED)) {
//                return true;
//            }
//
//            if (iteratorState.have(COMPLETE)) {
//                return false;
//            }
//            iteratorState.enable(REQUEST);
        this.prevSubscription.request(256);
        return true;
//        }
    }

    @Override
    public T next() {

        return queue.poll();
//        if (iteratorState.have(ARRIVED)) {
//            iteratorState.disable(ARRIVED | REQUEST);
//            return next;
//        }
//        if (iteratorState.have(COMPLETE)) {
//            throw new NoSuchElementException();
//        }
//        if (iteratorState.miss(REQUEST)) {
//            boolean hasNext = hasNext();
//            if (!hasNext) {
//                throw new NoSuchElementException();
//            }
//        }
//
//        iteratorState.disable(REQUEST | ARRIVED);
//        return next;
    }


}
