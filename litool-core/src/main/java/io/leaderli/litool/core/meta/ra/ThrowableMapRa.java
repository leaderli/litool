package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.exception.ThrowableInterfaceException;
import io.leaderli.litool.core.function.ThrowableFunction;
import io.leaderli.litool.core.meta.Lino;

/**
 * 忽视异常的 转换操作
 *
 * @author leaderli
 * @see Lino#throwable_map(ThrowableFunction)
 * @since 2022/6/27
 */
class ThrowableMapRa<T, R> extends Ra<R> {
    private final ThrowableFunction<? super T, ? extends R> mapper;
    private final Publisher<T> prevPublisher;


    public ThrowableMapRa(Publisher<T> prevPublisher, ThrowableFunction<? super T, ? extends R> mapper) {
        this.prevPublisher = prevPublisher;
        this.mapper = mapper;
    }

    @Override
    public void subscribe(Subscriber<? super R> actualSubscriber) {
        prevPublisher.subscribe(new ThrowableMapSubscriberSubscription(actualSubscriber));

    }

    private class ThrowableMapSubscriberSubscription extends IntermediateSubscriberSubscription<T, R> {


        private ThrowableMapSubscriberSubscription(Subscriber<? super R> actualSubscriber) {
            super(actualSubscriber);
        }


        @Override
        public void next(T t) {

            if (mapper == null) {
                this.actualSubscriber.next_null();
            } else {
                try {
                    SubscriberUtil.next(actualSubscriber, mapper.apply(t));
                } catch (Throwable e) {
                    throw new ThrowableInterfaceException(e);
                }
            }
        }


    }
}
