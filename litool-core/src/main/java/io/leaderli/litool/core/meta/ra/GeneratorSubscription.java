package io.leaderli.litool.core.meta.ra;

import java.util.Iterator;

/**
 * @author leaderli
 * @since 2022/9/3
 */
abstract class GeneratorSubscription<T> implements SubscriptionRa {

    protected final SubscriberRa<? super T> actualSubscriber;
    protected final Iterator<? extends T> iterator;

    protected boolean completed;

    protected GeneratorSubscription(SubscriberRa<? super T> actualSubscriber,
                                    Iterator<? extends T> iterator) {
        this.actualSubscriber = actualSubscriber;
        this.iterator = iterator;
    }

    @Override
    public final void cancel() {
        completed = true;
        actualSubscriber.onComplete();
    }
}
