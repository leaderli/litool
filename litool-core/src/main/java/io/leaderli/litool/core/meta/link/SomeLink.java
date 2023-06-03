package io.leaderli.litool.core.meta.link;

import io.leaderli.litool.core.function.Consumer;
import io.leaderli.litool.core.function.Function;
import io.leaderli.litool.core.function.Supplier;
import io.leaderli.litool.core.function.ThrowableRunner;
import io.leaderli.litool.core.meta.LiBox;
import io.leaderli.litool.core.meta.LiConstant;
import io.leaderli.litool.core.meta.LiLink;


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
    public <R> LiLink<R> map(java.util.function.Function<? super T, ? extends R> mapper) {
        return new MapLink<>(this, mapper);
    }


    @Override
    public LiLink<T> then(java.util.function.Function<? super T, ?> filter) {
        return new FilterLink<>(this, filter);
    }

    @Override
    public LiLink<T> then(java.util.function.Supplier<?> filter) {
        return new FilterLink<>(this, t -> filter.get());
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
    public LiLink<T> throwable_then(Function<? super T, ?> filter) {

        return new FilterLink<>(this, t -> {
            try {
                return filter.apply(t);
            } catch (Throwable e) {
                LiConstant.accept(e);
                return false;
            }
        });

    }

    @Override
    public LiLink<T> throwable_then(Supplier<?> filter) {
        return new FilterLink<>(this, t -> {
            try {
                return filter.get();
            } catch (Throwable e) {
                LiConstant.accept(e);
                return false;
            }
        });
    }


    @Override
    public LiLink<T> throwable_then(Consumer<? super T> consumer) {
        return new FilterLink<>(this, t -> {
            try {
                consumer.accept(t);
            } catch (Throwable e) {
                LiConstant.accept(e);
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
                LiConstant.accept(e);
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
                LiConstant.accept(e);
            }
        });
    }


    @Override
    public LiLink<T> onThrowableInterrupt(Consumer<? super T> consumer) {
        return new OnInterruptConsumerLink<>(this, v -> {
            try {
                consumer.accept(v);
            } catch (Throwable e) {
                LiConstant.accept(e);
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


