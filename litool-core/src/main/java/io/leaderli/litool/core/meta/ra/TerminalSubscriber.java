package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.bit.BitPermission;

import static io.leaderli.litool.core.meta.ra.IteratorStatus.COMPLETE;

/**
 * @author leaderli
 * @since 2022/9/1
 */
public class TerminalSubscriber<T> implements Subscriber<T> {


    private final Subscriber<T> delegate;
    private final BitPermission<IteratorStatus> iteratorState = new BitPermission<>();


    public TerminalSubscriber(Subscriber<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void onSubscribe(Subscription prevSubscription) {
        Subscription prevSubscription1 = prevSubscription.prevSubscription();
        this.delegate.onSubscribe(prevSubscription1);

        while (iteratorState.miss(COMPLETE)) {

            prevSubscription1.request();
        }

    }


    @Override
    public void next(T t) {

        iteratorState.enable(IteratorStatus.ARRIVED);
        delegate.next(t);
    }

    @Override
    public void onNull() {
        iteratorState.enable(IteratorStatus.ARRIVED);
        delegate.onNull();

    }

    @Override
    public void beforeRequest() {
        delegate.beforeRequest();
    }

    @Override
    public void onRequested() {
        delegate.onRequested();
    }

    @Override
    public void onComplete() {
        iteratorState.enable(COMPLETE);
        delegate.onComplete();
    }

    @Override
    public void onCancel() {
        iteratorState.enable(COMPLETE);
        delegate.onCancel();
    }

    @Override
    public void onError(Throwable t, CancelSubscription cancel) {
        delegate.onError(t, cancel);
    }
}
