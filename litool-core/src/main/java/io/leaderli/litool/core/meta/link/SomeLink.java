package io.leaderli.litool.core.meta.link;

import io.leaderli.litool.core.function.ThrowableConsumer;
import io.leaderli.litool.core.function.ThrowableFunction;
import io.leaderli.litool.core.function.ThrowableRunner;
import io.leaderli.litool.core.function.ThrowableSupplier;
import io.leaderli.litool.core.meta.LiBox;
import io.leaderli.litool.core.meta.LiLink;
import io.leaderli.litool.core.meta.WhenThrowBehavior;

import java.util.function.Function;
import java.util.function.Supplier;


abstract class SomeLink<P, T> implements LiLink<T> {

    protected final PublisherLink<P> prevPublisher;

    protected SomeLink(PublisherLink<P> prevPublisher) {
        this.prevPublisher = prevPublisher;
    }


    static LiLink<Integer> of() {

        return new ValueLink<>(1);
    }


    static <T> LiLink<T> of(T value) {

        return new ValueLink<>(value);
    }


    @Override
    public <R> LiLink<R> map(Function<? super T, ? extends R> mapper) {
        return new MapLink<>(this, mapper);
    }


    @Override
    public LiLink<T> then(java.util.function.Function<? super T, ?> filter) {
        return new FilterLink<>(this, filter);
    }

    @Override
    public LiLink<T> then(Supplier<?> supplier) {
        return new FilterLink<>(this, t -> supplier.get());
    }


    @Override
    public LiLink<T> then(java.util.function.Consumer<? super T> consumer) {
        return new FilterLink<>(this, t -> {
            consumer.accept(t);
            return true;
        });
    }

    @Override
    public LiLink<T> then(Runnable runnable) {
        return new FilterLink<>(this, t -> {
            runnable.run();
            return true;
        });
    }


    @Override
    public LiLink<T> throwable_then(ThrowableFunction<? super T, ?> filter) {

        return new FilterLink<>(this, t -> {
            try {
                return filter.apply(t);
            } catch (Throwable e) {
                WhenThrowBehavior.whenThrow(e);
                return false;
            }
        });

    }

    @Override
    public LiLink<T> throwable_then(ThrowableSupplier<?> supplier) {
        return new FilterLink<>(this, t -> {
            try {
                return supplier.get();
            } catch (Throwable e) {
                WhenThrowBehavior.whenThrow(e);
                return false;
            }
        });
    }


    @Override
    public LiLink<T> throwable_then(ThrowableConsumer<? super T> consumer) {
        return new FilterLink<>(this, t -> {
            try {
                consumer.accept(t);
            } catch (Throwable e) {
                WhenThrowBehavior.whenThrow(e);
                return false;
            }
            return true;
        });
    }


    @Override
    public LiLink<T> throwable_then(ThrowableRunner runner) {
        return new FilterLink<>(this, t -> {
            try {
                runner.run();
            } catch (Throwable e) {
                WhenThrowBehavior.whenThrow(e);
                return false;
            }
            return true;
        });
    }


    @Override
    public LiLink<T> onInterrupt(Runnable runnable) {

        return new OnInterruptRunnableLink<>(this, runnable);
    }

    @Override
    public LiLink<T> onInterrupt(java.util.function.Consumer<? super T> consumer) {
        return new OnInterruptConsumerLink<>(this, consumer);
    }


    @Override
    public LiLink<T> onThrowableInterrupt(ThrowableRunner runnable) {

        return new OnInterruptRunnableLink<>(this, () -> {
            try {
                runnable.run();
            } catch (Throwable e) {
                WhenThrowBehavior.whenThrow(e);
            }
        });
    }


    @Override
    public LiLink<T> onThrowableInterrupt(ThrowableConsumer<? super T> consumer) {
        return new OnInterruptConsumerLink<>(this, v -> {
            try {
                consumer.accept(v);
            } catch (Throwable e) {
                WhenThrowBehavior.whenThrow(e);
            }

        });
    }


    @Override
    public boolean present() {
        LiBox<Object> next = LiBox.none();
        this.subscribe(new SubscriberLink<T>() {
            @Override
            public void onSubscribe(SubscriptionLink prevSubscription) {
                prevSubscription.request();
            }

            @Override
            public void next(T value) {
                next.value(value);

            }
        });

        return next.present();
    }

    @Override
    public String name() {
        return "link";
    }


    @Override
    public void onFinally(java.util.function.Consumer<Boolean> onFinally) {
        onFinally.accept(present());
    }


    @Override
    public void run() {
        present();
    }


}


