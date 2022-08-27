package io.leaderli.litool.core.meta.link;

import io.leaderli.litool.core.meta.Lino;

/**
 * @author leaderli
 * @since 2022/7/18
 * <p>
 * 前后类型相同
 */
public abstract class SameTypeIntermediateSubscriberLink<T> extends IntermediateSubscriberLink<T, T> {
protected SameTypeIntermediateSubscriberLink(SubscriberLink<T> actualSubscriber) {
    super(actualSubscriber);
}


@Override
public void onCancel(Lino<T> lino) {
    this.actualSubscriber.onCancel(lino);
}


@Override
public void request(T t) {
    this.prevSubscription.request(t);
}
}
