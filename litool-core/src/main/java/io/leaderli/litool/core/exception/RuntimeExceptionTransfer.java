package io.leaderli.litool.core.exception;

import io.leaderli.litool.core.function.ThrowableConsumer;
import io.leaderli.litool.core.function.ThrowableFunction;
import io.leaderli.litool.core.function.ThrowableRunner;
import io.leaderli.litool.core.function.ThrowableSupplier;

/**
 * @author leaderli
 * @since 2022/6/16
 */
public class RuntimeExceptionTransfer {

    public static void run(ThrowableRunner runnable) {
        try {
            runnable.run();
        } catch (Throwable throwable) {
            throw new RuntimeExceptionTransferException(throwable);
        }
    }

    public static <T> void accept(ThrowableConsumer<T> consumer, T t) {
        try {
            consumer.accept(t);
        } catch (Throwable throwable) {
            throw new RuntimeExceptionTransferException(throwable);
        }
    }

    public static <T, R> R apply(ThrowableFunction<T, R> function, T t) {
        try {
            return function.apply(t);
        } catch (Throwable throwable) {
            throw new RuntimeExceptionTransferException(throwable);
        }
    }

    public static <T> T get(ThrowableSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Throwable throwable) {
            throw new RuntimeExceptionTransferException(throwable);
        }
    }
}
