package io.leaderli.litool.core.meta.ra;

import io.leaderli.litool.core.function.ThrowableFunction;
import io.leaderli.litool.core.meta.Lino;

import java.util.function.Consumer;

/**
 * 忽视异常的 转换操作
 *
 * @author leaderli
 * @see Lino#throwable_map(ThrowableFunction)
 * @since 2022/6/27
 */
public class ThrowableMapRa<T, R> extends SomeRa<R> {
    private final ThrowableFunction<? super T, ? extends R> mapper;
    private final PublisherRa<T> prevPublisher;
    private final Consumer<Throwable> whenThrow;

    public ThrowableMapRa(PublisherRa<T> prevPublisher, ThrowableFunction<? super T, ? extends R> mapper, Consumer<Throwable> whenThrow) {
        this.prevPublisher = prevPublisher;
        this.mapper = mapper;
        this.whenThrow = whenThrow;
    }

    @Override
    public void subscribe(SubscriberRa<? super R> actualSubscriber) {
        prevPublisher.subscribe(new ThrowableMapSubscriberRa(actualSubscriber));

    }

    private class ThrowableMapSubscriberRa extends IntermediateSubscriberRa<T, R> {


        private ThrowableMapSubscriberRa(SubscriberRa<? super R> actualSubscriber) {
            super(actualSubscriber);
        }


        @Override
        public void next(T t) {
            Lino.of(t).throwable_map(mapper, whenThrow).ifPresent(this.actualSubscriber::next);
        }

    }
}
