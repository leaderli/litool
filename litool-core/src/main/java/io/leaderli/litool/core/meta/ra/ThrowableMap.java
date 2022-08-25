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
public class ThrowableMap<T, R> extends Some<R> {
    private final ThrowableFunction<? super T, ? extends R> mapper;
    private final Publisher<T> prevPublisher;
    private final Consumer<Throwable> whenThrow;

    public ThrowableMap(Publisher<T> prevPublisher, ThrowableFunction<? super T, ? extends R> mapper, Consumer<Throwable> whenThrow) {
        this.prevPublisher = prevPublisher;
        this.mapper = mapper;
        this.whenThrow = whenThrow;
    }

    @Override
    public void subscribe(Subscriber<? super R> actualSubscriber) {
        prevPublisher.subscribe(new ThrowableMapSubscriber(actualSubscriber));

    }

    private class ThrowableMapSubscriber extends IntermediateSubscriber<T, R> {


        private ThrowableMapSubscriber(Subscriber<? super R> actualSubscriber) {
            super(actualSubscriber);
        }


        @Override
        public void next(T t) {
            Lino.of(t).map(m -> {
                try {
                    return mapper.apply(m);
                } catch (Throwable throwable) {
                    this.actualSubscriber.onError(throwable, this);
                }
                return null;
            }).ifPresent(this.actualSubscriber::next);
        }

    }
}
