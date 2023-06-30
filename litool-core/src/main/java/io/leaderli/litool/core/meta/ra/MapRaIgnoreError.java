package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.exception.MapperRuntimeException;
import io.leaderli.litool.core.function.ThrowableFunction;
import io.leaderli.litool.core.meta.Lino;

import java.util.Objects;

/**
 * 忽视异常的 转换操作
 *
 * @author leaderli
 * @see Lino#mapIgnoreError(ThrowableFunction)
 * @since 2022/6/27
 */
class MapRaIgnoreError<T, R> extends Ra<R> {
    private final ThrowableFunction<? super T, ? extends R> mapper;
    private final PublisherRa<T> prevPublisher;


    public MapRaIgnoreError(PublisherRa<T> prevPublisher, ThrowableFunction<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper);
        this.prevPublisher = prevPublisher;
        this.mapper = mapper;
    }

    @Override
    public void subscribe(SubscriberRa<? super R> actualSubscriber) {
        prevPublisher.subscribe(new MapIgnoreErrorSubscriberSubscription(actualSubscriber));

    }

    private class MapIgnoreErrorSubscriberSubscription extends IntermediateSubscriberSubscription<T, R> {


        private MapIgnoreErrorSubscriberSubscription(SubscriberRa<? super R> actualSubscriber) {
            super(actualSubscriber);
        }


        @Override
        public void next(T t) {

            try {
                SubscriberUtil.next(actualSubscriber, mapper.apply(t));
            } catch (Throwable e) {
                throw new MapperRuntimeException(e);
            }
        }


    }
}
