package io.leaderli.litool.core.exception;

import io.leaderli.litool.core.function.ThrowableConsumer;
import io.leaderli.litool.core.function.ThrowableFunction;
import io.leaderli.litool.core.function.ThrowableRunner;
import io.leaderli.litool.core.function.ThrowableSupplier;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * wrapper the {@link  Throwable} to {@link RuntimeException}
 *
 * @author leaderli
 * @since 2022/6/16
 */
public class RuntimeExceptionTransfer {
    /**
     * 将 {@link ThrowableRunner} 包装为 {@link Runnable}
     *
     * @param runnable 可能会抛出异常的 Runnable
     * @return 包装后的 Runnable
     */
    public static Runnable of(ThrowableRunner runnable) {

        return () -> run(runnable);
    }

    /**
     * 将 {@link ThrowableConsumer} 包装为 {@link java.util.function.Consumer}
     *
     * @param consumer 可能会抛出异常的 Consumer
     * @param <T>      函数的参数类型
     * @return 包装后的 Consumer
     */
    public static <T> Consumer<T> of(ThrowableConsumer<T> consumer) {
        return t -> accept(consumer, t);
    }

    /**
     * 执行 Consumer，如果发生异常则将其包装为 RuntimeException 抛出
     *
     * @param consumer 可能会抛出异常的 Consumer
     * @param t        Consumer 的参数
     * @param <T>      函数的第一个参数类型
     */
    public static <T> void accept(ThrowableConsumer<T> consumer, T t) {
        try {
            consumer.accept(t);
        } catch (Throwable throwable) {
            throw ExceptionUtil.toRuntimeException(throwable);
        }
    }

    /**
     * @param <T>      函数的第一个参数类型
     * @param <R>      函数的第二个参数类型
     *                 将 {@link ThrowableFunction} 包装为 {@link java.util.function.Function}
     * @param function 可能会抛出异常的 Function
     * @return 包装后的 Function
     */
    public static <T, R> Function<T, R> of(ThrowableFunction<T, R> function) {
        return t -> apply(function, t);
    }

    /**
     * 执行 Runnable，如果发生异常则将其包装为 RuntimeException 抛出
     *
     * @param runnable 可能会抛出异常的 Runnable
     */
    public static void run(ThrowableRunner runnable) {
        try {
            runnable.run();
        } catch (Throwable throwable) {
            throw ExceptionUtil.toRuntimeException(throwable);
        }
    }

    /**
     * 执行 Function，如果发生异常则将其包装为 RuntimeException 抛出
     *
     * @param function 可能会抛出异常的 Function
     * @param t        Function 的参数
     * @param <T>      函数的第一个参数类型
     * @param <R>      函数的第二个参数类型
     * @return Function 的执行结果
     */
    public static <T, R> R apply(ThrowableFunction<T, R> function, T t) {
        try {
            return function.apply(t);
        } catch (Throwable throwable) {
            throw ExceptionUtil.toRuntimeException(throwable);
        }
    }

    /**
     * 将 {@link ThrowableSupplier} 包装为 {@link java.util.function.Supplier}
     *
     * @param supplier 可能会抛出异常的 Supplier
     * @param <T>      函数的第一个参数类型
     * @return 包装后的 Supplier
     */
    public static <T> Supplier<T> of(ThrowableSupplier<T> supplier) {
        return () -> get(supplier);
    }

    /**
     * @param <T>      函数的参数类型
     *                 执行 Supplier，如果发生异常则将其包装为 RuntimeException 抛出
     * @param supplier 可能会抛出异常的 Supplier
     * @return Supplier 的执行结果
     */
    public static <T> T get(ThrowableSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Throwable throwable) {
            throw ExceptionUtil.toRuntimeException(throwable);
        }
    }
}
