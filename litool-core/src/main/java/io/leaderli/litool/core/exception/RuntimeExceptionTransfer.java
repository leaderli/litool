package io.leaderli.litool.core.exception;

import io.leaderli.litool.core.function.Consumer;
import io.leaderli.litool.core.function.Function;
import io.leaderli.litool.core.function.Supplier;
import io.leaderli.litool.core.function.ThrowableRunner;

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
     * 将 {@link Consumer} 包装为 {@link java.util.function.Consumer}
     *
     * @param consumer 可能会抛出异常的 Consumer
     * @return 包装后的 Consumer
     */
    public static <T> java.util.function.Consumer<T> of(Consumer<T> consumer) {
        return t -> accept(consumer, t);
    }

    /**
     * 将 {@link Function} 包装为 {@link java.util.function.Function}
     *
     * @param function 可能会抛出异常的 Function
     * @return 包装后的 Function
     */
    public static <T, R> java.util.function.Function<T, R> of(Function<T, R> function) {
        return t -> apply(function, t);
    }

    /**
     * 将 {@link Supplier} 包装为 {@link java.util.function.Supplier}
     *
     * @param supplier 可能会抛出异常的 Supplier
     * @return 包装后的 Supplier
     */
    public static <T> java.util.function.Supplier<T> of(Supplier<T> supplier) {
        return () -> get(supplier);
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
            throw new RuntimeExceptionTransferException(throwable);
        }
    }

    /**
     * 执行 Consumer，如果发生异常则将其包装为 RuntimeException 抛出
     *
     * @param consumer 可能会抛出异常的 Consumer
     * @param t        Consumer 的参数
     */
    public static <T> void accept(Consumer<T> consumer, T t) {
        try {
            consumer.accept(t);
        } catch (Throwable throwable) {
            throw new RuntimeExceptionTransferException(throwable);
        }
    }

    /**
     * 执行 Function，如果发生异常则将其包装为 RuntimeException 抛出
     *
     * @param function 可能会抛出异常的 Function
     * @param t        Function 的参数
     * @return Function 的执行结果
     */
    public static <T, R> R apply(Function<T, R> function, T t) {
        try {
            return function.apply(t);
        } catch (Throwable throwable) {
            throw new RuntimeExceptionTransferException(throwable);
        }
    }

    /**
     * 执行 Supplier，如果发生异常则将其包装为 RuntimeException 抛出
     *
     * @param supplier 可能会抛出异常的 Supplier
     * @return Supplier 的执行结果
     */
    public static <T> T get(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Throwable throwable) {
            throw new RuntimeExceptionTransferException(throwable);
        }
    }
}
