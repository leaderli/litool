package io.leaderli.litool.core.meta;

import io.leaderli.litool.core.exception.LiThrowableFunction;

import java.util.function.Consumer;

/**
 * @author leaderli
 * @since 2022/6/27
 */
public class RaThrowableMap<T, R> extends RaSome<R> {
    private final LiThrowableFunction<? super T, ? extends R> mapper;
    private final RaPublisher<T> prevPublisher;
    private final Consumer<Throwable> whenThrow;

    public RaThrowableMap(RaPublisher<T> prevPublisher, LiThrowableFunction<? super T, ? extends R> mapper, Consumer<Throwable> whenThrow) {
        this.prevPublisher = prevPublisher;
        this.mapper = mapper;
        this.whenThrow = whenThrow;
    }

    @Override
    public void subscribe(RaSubscriber<? super R> actualSubscriber) {
        prevPublisher.subscribe(new ThrowableMapRaSubscriber<>(actualSubscriber, mapper, whenThrow));

    }

    private static class ThrowableMapRaSubscriber<T, R> extends IntermediateRaSubscriber<T, R> {

        private final LiThrowableFunction<? super T, ? extends R> mapper;
        private final Consumer<Throwable> whenThrow;

        private ThrowableMapRaSubscriber(RaSubscriber<? super R> actualSubscriber, LiThrowableFunction<? super T, ? extends R> mapper, Consumer<Throwable> whenThrow) {
            super(actualSubscriber);
            this.mapper = mapper;
            this.whenThrow = whenThrow;
        }


        @Override
        public void next(Lino<T> t) {
            t.throwable_map(mapper,whenThrow).nest(f -> this.actualSubscriber.next(Lino.narrow(f)));
        }

    }
}
