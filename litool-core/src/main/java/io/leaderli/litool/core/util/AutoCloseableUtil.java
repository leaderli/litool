package io.leaderli.litool.core.util;

import io.leaderli.litool.core.exception.AutoCloseRuntimeException;
import io.leaderli.litool.core.function.ThrowableConsumer;
import io.leaderli.litool.core.function.ThrowableFunction;
import io.leaderli.litool.core.function.ThrowableSupplier;

/**
 * 提供一个规范关闭可关闭资源的工具类
 *
 * @author leaderli
 * @since 2022/9/5
 */
public class AutoCloseableUtil {


    /**
     * 使用 try-resource 风格的代码，在执行 action 之后自动调用 {@link AutoCloseable#close()} 方法。
     * <p>
     * Use try-resource style code to automatically invoke the {@link AutoCloseable#close()} method
     * <p>
     * after the action has been performed.
     *
     * @param <T>      扩展了 {@link AutoCloseable} 接口的类型
     * @param supplier 提供 {@link AutoCloseable} 实例的 supplier
     * @param consumer 消费 {@link AutoCloseable} 实例的 consumer
     */
    public static <T extends AutoCloseable> void autoCloseConsumer(ThrowableSupplier<T> supplier, ThrowableConsumer<T> consumer) {

        try (T closeable = supplier.get()) {
            consumer.accept(closeable);
        } catch (Throwable e) {
            throw new AutoCloseRuntimeException(e);
        }
    }

    /**
     * 使用 try-resource 风格的代码，在执行 action 之后自动调用 {@link AutoCloseable#close()} 方法。
     * <p>
     * Use try-resource style code to automatically invoke the {@link AutoCloseable#close()} method
     * <p>
     * after the action has been performed.
     *
     * @param <T>      扩展了 {@link AutoCloseable} 接口的类型
     * @param <R>      函数提供的结果类型
     * @param supplier 提供 {@link AutoCloseable} 实例的 supplier
     * @param function 函数
     * @return 函数的结果
     */
    public static <T extends AutoCloseable, R> R autoCloseFunction(ThrowableSupplier<T> supplier, ThrowableFunction<T, R> function) {

        try (T closeable = supplier.get()) {
            return function.apply(closeable);
        } catch (Throwable e) {
            throw new AutoCloseRuntimeException(e);
        }
    }
}
