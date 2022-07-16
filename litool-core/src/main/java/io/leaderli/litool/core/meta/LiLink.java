package io.leaderli.litool.core.meta;

import io.leaderli.litool.core.exception.LiThrowableFunction;
import io.leaderli.litool.core.exception.LiThrowableSupplier;
import io.leaderli.litool.core.meta.reactor.*;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @param <T> 泛型
 *            <p>
 *            链式执行，每个节点执行成功后才会执行下一个节点，若某一个节点执行结果为失败，
 *            则调用 {@link #error(Runnable)}。
 *            执行动作是响应式的
 * @author leaderli
 * @since 2022/7/16
 */
public abstract class LiLink<T> implements LiValue, PublisherLink<T> {

    /**
     * @return 返回一个值为 1 的实例
     */
    static LiLink<Integer> of() {

        return new SomeLink<>(1);
    }

    /**
     * @param value 实例
     * @param <T>   泛型
     * @return 返回一个新的实例
     */
    static <T> LiLink<T> of(T value) {

        return new SomeLink<>(value);
    }


    public LiLink<T> then(Function<T, Object> filter) {
        return new FilterLink<>(this, filter);
    }

    public LiLink<T> throwable_then(LiThrowableFunction<T, Object> function) {

        return new FilterLink<>(this, t -> {
            try {
                return function.apply(t);
            } catch (Throwable e) {
                LiConstant.accept(e);
                return false;
            }
        });

    }

    public LiLink<T> then(Supplier<Object> supplier) {
        return new FilterLink<>(this, t -> supplier.get());
    }

    public LiLink<T> throwable_then(LiThrowableSupplier<Object> supplier) {
        return new FilterLink<>(this, t -> {
            try {
                return supplier.get();
            } catch (Throwable e) {
                LiConstant.accept(e);
                return false;
            }
        });
    }

    public LiLink<T> error(Runnable runnable) {

        return new CancelRunnableLink<>(this, runnable);
    }

    public CancelConsumerLink<T> error(Consumer<? super T> runnable) {
        return new CancelConsumerLink<>(this, runnable);
    }

    @Override
    public boolean present() {
        LiBox<Object> next = LiBox.none();
        this.subscribe(new SubscriberLink<T>() {
            @Override
            public void onSubscribe(SubscriptionLink<T> prevSubscription) {
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


}


