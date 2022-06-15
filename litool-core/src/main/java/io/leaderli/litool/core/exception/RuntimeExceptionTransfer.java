package io.leaderli.litool.core.exception;

/**
 * @author leaderli
 * @since 2022/6/16
 */
public class RuntimeExceptionTransfer {

    public static void run(LiThrowableRunner runnable) {
        try {
            runnable.run();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public static <T> void accept(LiThrowableConsumer<T> consumer, T t) {
        try {
            consumer.accept(t);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public static <T, R> R apply(LiThrowableFunction<T, R> function, T t) {
        try {
            return function.apply(t);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public static <T> T get(LiThrowableSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
