package io.leaderli.litool.core.meta.link;

import io.leaderli.litool.core.function.ThrowableConsumer;
import io.leaderli.litool.core.function.ThrowableFunction;
import io.leaderli.litool.core.function.ThrowableRunner;
import io.leaderli.litool.core.function.ThrowableSupplier;
import io.leaderli.litool.core.meta.LiBox;
import io.leaderli.litool.core.meta.LiConstant;
import io.leaderli.litool.core.util.BooleanUtil;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @param <T> 泛型
 *            <p>
 *            链式执行，每个节点执行成功后才会执行下一个节点，若某一个节点执行结果为失败，
 *            则调用最靠近的一组  error 节点
 *            执行动作是响应式的， 需要显式的调用 {@link #present()} 才会触发
 * @author leaderli
 * @since 2022/7/16
 */
abstract class SomeLink<P, T> implements LiLink<T> {

    protected final Publisher<P> prevPublisher;

    protected SomeLink(Publisher<P> prevPublisher) {
        this.prevPublisher = prevPublisher;
    }

    /**
     * @return 返回一个值为 1 的实例
     */
    static LiLink<Integer> of() {

        return new ValueLink<>(1);
    }

    /**
     * @param value 实例
     * @param <T>   泛型
     * @return 返回一个新的实例
     */
    static <T> LiLink<T> of(T value) {

        return new ValueLink<>(value);
    }


    @Override
    public <R> LiLink<R> map(Function<? super T, ? extends R> mapper) {
        return new MapLink<>(this, mapper);
    }

    @Override
    public <R> LiLink<R> union(R value) {
        return new UnionLink<>(this, () -> value);
    }

    @Override
    public <R> LiLink<R> union(Supplier<R> supplier) {
        return new UnionLink<>(this, supplier);
    }

    /**
     * 当返回 false 时 ，跳过执行后续的  filter,then, 执行最近的 连续的 error 节点，并且终止执行
     *
     * @param filter 过滤器
     * @return this
     * @see BooleanUtil#parse(Object)
     */
    @Override
    public LiLink<T> then(Function<? super T, ?> filter) {
        return new FilterLink<>(this, filter);
    }

    /**
     * @param supplier 过滤器
     * @return this
     * @see #then(Function)
     */
    @Override
    public LiLink<T> then(Supplier<?> supplier) {
        return new FilterLink<>(this, t -> supplier.get());
    }

    /**
     * 未中断链条时执行，且继续执行下一个节点，该节点不会捕获异常，
     *
     * @param consumer 消费者
     * @return this
     */
    @Override
    public LiLink<T> then(Consumer<? super T> consumer) {
        return new FilterLink<>(this, t -> {
            consumer.accept(t);
            return true;
        });
    }

    /**
     * 未中断链条时执行，且继续执行下一个节点，该节点不会捕获异常，
     *
     * @param runnable 运行函数
     * @return this
     */
    @Override
    public LiLink<T> then(Runnable runnable) {
        return new FilterLink<>(this, t -> {
            runnable.run();
            return true;
        });
    }


    /**
     * 当抛出异常时，中断链条执行
     *
     * @param filter 过滤器
     * @return this
     * @see #then(Function)
     */
    @Override
    public LiLink<T> throwable_then(ThrowableFunction<? super T, ?> filter) {

        return new FilterLink<>(this, t -> {
            try {
                return filter.apply(t);
            } catch (Throwable e) {
                LiConstant.accept(e);
                return false;
            }
        });

    }

    /**
     * 当抛出异常时，中断链条执行
     *
     * @param filter 过滤器
     * @return this
     * @see #then(Supplier)
     */
    @Override
    public LiLink<T> throwable_then(ThrowableSupplier<?> filter) {
        return new FilterLink<>(this, t -> {
            try {
                return filter.get();
            } catch (Throwable e) {
                LiConstant.accept(e);
                return false;
            }
        });
    }

    /**
     * 当抛出异常时，中断链条执行
     *
     * @param consumer 消费者
     * @return this
     * @see #then(Consumer)
     */
    @Override
    public LiLink<T> throwable_then(ThrowableConsumer<? super T> consumer) {
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

    /**
     * 当抛出异常时，中断链条执行
     *
     * @param runner 运行函数
     * @return this
     * @see #then(Runnable)
     */
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


    /**
     * 当链条失败时执行
     *
     * @param runnable 执行函数
     * @return this
     */
    @Override
    public LiLink<T> error(Runnable runnable) {

        return new OnErrorRunnableLink<>(this, runnable);
    }

    /**
     * 当链条失败且 value 不为 null 时执行
     *
     * @param consumer 消费者
     * @return this
     */
    @Override
    public OnErrorConsumerLink<T> error(Consumer<? super T> consumer) {
        return new OnErrorConsumerLink<>(this, consumer);
    }

    /**
     * 当链条失败时执行，无视异常
     *
     * @param runnable 执行函数
     * @return this
     * @see LiConstant#WHEN_THROW
     */
    @Override
    public LiLink<T> throwable_error(ThrowableRunner runnable) {

        return new OnErrorRunnableLink<>(this, () -> {
            try {
                runnable.run();
            } catch (Throwable e) {
                LiConstant.accept(e);
            }
        });
    }

    /**
     * 当链条失败且 value 不为 null 时执行，无视异常
     *
     * @param consumer 消费者
     * @return this
     * @see LiConstant#WHEN_THROW
     */
    @Override
    public OnErrorConsumerLink<T> throwable_error(ThrowableConsumer<? super T> consumer) {
        return new OnErrorConsumerLink<>(this, v -> {
            try {
                consumer.accept(v);
            } catch (Throwable e) {
                LiConstant.accept(e);
            }

        });
    }

    /**
     * @return 链条是否正确执行完成，没有任何 error 节点执行
     */
    @Override
    public boolean present() {
        LiBox<Object> next = LiBox.none();
        this.subscribe(new Subscriber<T>() {
            @Override
            public void onSubscribe(Subscription<T> prevSubscription) {
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
    public void request(final T t) {
        new NewRequestLink<>(this, t).run();
    }

    /**
     * @param onFinally 最后一个消费者
     * @see #present()
     */
    @Override
    public void onFinally(Consumer<Boolean> onFinally) {
        onFinally.accept(present());
    }

    /**
     * 触发执行动作
     *
     * @see #present()
     */
    @Override
    public void run() {
        present();
    }


}


