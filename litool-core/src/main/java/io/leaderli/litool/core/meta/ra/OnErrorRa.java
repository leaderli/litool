package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.util.BooleanUtil;

import java.util.function.Function;

/**
 * 过滤不符合断言的元素
 *
 * @author leaderli
 * @see Lino#filter(Function)
 * @see BooleanUtil#parse(Object)
 * @since 2022/6/27
 */
class OnErrorRa<T> extends PublisherRa<T> {


    private final Exceptionable onError;

    public OnErrorRa(Publisher<T> prevPublisher, Exceptionable onError) {
        super(prevPublisher);
        this.onError = onError;
    }

    @Override
    public void subscribe(Subscriber<? super T> actualSubscriber) {
        prevPublisher.subscribe(new OnErrorSubscriberSubscription(actualSubscriber));

    }


    private final class OnErrorSubscriberSubscription extends IntermediateSubscriberSubscription<T, T> {


        public OnErrorSubscriberSubscription(Subscriber<? super T> actualSubscriber) {
            super(actualSubscriber);
        }

        @Override
        public void next(T t) {
            actualSubscriber.next(t);
        }

        @Override
        public void onError(Throwable t, CancelSubscription cancel) {
            onError.onError(t, cancel);
        }
    }
}
