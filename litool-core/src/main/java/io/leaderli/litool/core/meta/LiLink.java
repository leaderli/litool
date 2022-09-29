package io.leaderli.litool.core.meta;

import io.leaderli.litool.core.function.ThrowableConsumer;
import io.leaderli.litool.core.function.ThrowableFunction;
import io.leaderli.litool.core.function.ThrowableRunner;
import io.leaderli.litool.core.function.ThrowableSupplier;
import io.leaderli.litool.core.meta.link.PublisherLink;
import io.leaderli.litool.core.meta.link.ValueLink;
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
public interface LiLink<T> extends LiValue, PublisherLink<T>, Runnable {

    /**
     * @return a liLink with value 1, the value only to drive chain execute
     */
    static LiLink<Integer> of() {

        return new ValueLink<>(1);
    }

    /**
     * @param <T> the type of value
     * @return a none liLink
     */
    static <T> LiLink<T> none() {

        return new ValueLink<>(null);
    }

    /**
     * @param value a value
     * @param <T>   the type of value
     * @return a liLink
     */
    static <T> LiLink<T> of(T value) {

        return new ValueLink<>(value);
    }

    /**
     * @param supplier a value provider
     * @param <T>      the type of value
     * @return a liLink
     */
    static <T> LiLink<T> supplier(Supplier<T> supplier) {

        if (supplier == null) {
            return new ValueLink<>(null);
        }
        return new ValueLink<>(supplier.get());
    }

    /**
     * when the filter throw a exception or it result is null, interrupt the execution chain
     *
     * @param mapper the mapper function
     * @param <R>    the type of mapper result
     * @return new link
     */
    <R> LiLink<R> map(Function<? super T, ? extends R> mapper);


    /**
     * when the filter throw a exception or it result is false, interrupt the execution chain
     *
     * @param filter the filter
     * @return new link
     * @see BooleanUtil#parse(Object)
     */
    LiLink<T> then(Function<? super T, ?> filter);

    /**
     * when the supplier throw a exception, interrupt the execution chain
     *
     * @param supplier the supplier
     * @return new link
     */
    LiLink<T> then(Supplier<?> supplier);

    /**
     * when the runnable throw a exception, interrupt the execution chain
     *
     * @param consumer the consumer
     * @return new link
     */
    LiLink<T> then(Consumer<? super T> consumer);

    /**
     * when the runnable throw a exception, interrupt the execution chain
     *
     * @param runnable the runnable
     * @return new link
     */
    LiLink<T> then(Runnable runnable);


    /**
     * when the filter throw a exception or it result is false, interrupt the execution chain
     *
     * @param filter the filter
     * @return new link
     * @see BooleanUtil#parse(Object)
     */
    LiLink<T> throwable_then(ThrowableFunction<? super T, ?> filter);

    /**
     * when the supplier throw a exception, interrupt the execution chain
     *
     * @param supplier the supplier
     * @return new link
     */
    LiLink<T> throwable_then(ThrowableSupplier<?> supplier);

    /**
     * when the consumer throw a exception, interrupt the execution chain
     *
     * @param consumer the consumer
     * @return new link
     */
    LiLink<T> throwable_then(ThrowableConsumer<? super T> consumer);

    /**
     * when the runnable throw a exception, interrupt the execution chain
     *
     * @param runner the runnable
     * @return new link
     */
    LiLink<T> throwable_then(ThrowableRunner runner);


    /**
     * runnable only run when the execution chain has interrupt
     *
     * @param runnable the runnable
     * @return new link
     * @see LiConstant#WHEN_THROW
     */
    LiLink<T> onInterrupt(Runnable runnable);

    /**
     * consumer only accept when the execution chain has interrupt and the  notify value is not null
     *
     * @param consumer the consumer
     * @return new link
     * @see LiConstant#WHEN_THROW
     */
    LiLink<T> onInterrupt(Consumer<? super T> consumer);

    /**
     * runnable only run when the execution chain has interrupt
     *
     * @param runnable the runnable
     * @return new link
     * @see LiConstant#WHEN_THROW
     */
    LiLink<T> onThrowableInterrupt(ThrowableRunner runnable);

    /**
     * consumer only accept when the execution chain has interrupt and the  notify value is not null
     *
     * @param consumer the consumer
     * @return new link
     * @see LiConstant#WHEN_THROW
     */
    LiLink<T> onThrowableInterrupt(ThrowableConsumer<? super T> consumer);

    /**
     * the terminal action
     *
     * @return {@code  true} if the execution chain has not interrupted
     */
    @Override
    boolean present();

    @Override
    String name();


    /**
     * the terminal action, call finally with the parameter of whether the execution chains is not interrupted
     *
     * @param onFinally the last consumer, will always called
     * @see #present()
     */
    void onFinally(Consumer<Boolean> onFinally);

    /**
     * the terminal action,  behavior like {@link  Runnable}
     *
     * @see #present()
     */
    @Override
    void run();


}


