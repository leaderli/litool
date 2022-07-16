package io.leaderli.litool.core.meta.reactor;

import io.leaderli.litool.core.exception.LiThrowableFunction;
import io.leaderli.litool.core.meta.Lino;

import java.util.function.Consumer;

/**
 * 忽视异常的 转换操作
 *
 * @author leaderli
 * @see Lino#throwable_map(LiThrowableFunction)
 * @since 2022/6/27
 */
public class ThrowableMapRa<T, R> extends SomeRa<R> {
    private final LiThrowableFunction<? super T, ? extends R> mapper;
    private final PublisherRa<T> prevPublisher;
    private final Consumer<Throwable> whenThrow;

    public ThrowableMapRa(PublisherRa<T> prevPublisher, LiThrowableFunction<? super T, ? extends R> mapper, Consumer<Throwable> whenThrow) {
        this.prevPublisher = prevPublisher;
        this.mapper = mapper;
        this.whenThrow = whenThrow;
    }

    @Override
    public void subscribe(SubscriberRa<? super R> actualSubscriber) {
        prevPublisher.subscribe(new ThrowableMapSubscriberRa<>(actualSubscriber, mapper, whenThrow));

    }

    private static class ThrowableMapSubscriberRa<T, R> extends IntermediateSubscriberRa<T, R> {

        private final LiThrowableFunction<? super T, ? extends R> mapper;
        private final Consumer<Throwable> whenThrow;

        private ThrowableMapSubscriberRa(SubscriberRa<? super R> actualSubscriber, LiThrowableFunction<? super T, ? extends R> mapper, Consumer<Throwable> whenThrow) {
            super(actualSubscriber);
            this.mapper = mapper;
            this.whenThrow = whenThrow;
        }


        @Override
        public void next(Lino<? extends T> t) {
            t.throwable_map(mapper, whenThrow).nest(this.actualSubscriber::next);
        }

    }
}