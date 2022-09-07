package io.leaderli.litool.core.meta.link;

import io.leaderli.litool.core.meta.Lino;

/**
 * @author leaderli
 * @since 2022/7/18
 * <p>
 * 前后类型相同
 */
abstract class SameTypeIntermediateSubscriber<T> extends IntermediateSubscriber<T, T> {
    protected SameTypeIntermediateSubscriber(Subscriber<T> actualSubscriber) {
        super(actualSubscriber);
    }


    @Override
    public void onError(Lino<T> lino) {
        this.actualSubscriber.onError(lino);
    }


    @Override
    public void request(T t) {
        this.prevSubscription.request(t);
    }
}
