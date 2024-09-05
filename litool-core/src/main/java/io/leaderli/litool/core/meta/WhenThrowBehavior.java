package io.leaderli.litool.core.meta;

import io.leaderli.litool.core.function.OnError;

import java.util.function.Supplier;

/**
 * litool工具类内部抛出异常时的默认行为
 */
public class WhenThrowBehavior {


    public static final ThreadLocal<OnError> ERROR_THREAD_LOCAL = ThreadLocal.withInitial(() -> Throwable::printStackTrace);
    public static final OnError PRINT_STACK = t -> ERROR_THREAD_LOCAL.get().onError(t);

    /**
     * 使用默认异常消费者接受一个异常
     *
     * @param e 异常
     */
    public static void whenThrow(Throwable e) {
        PRINT_STACK.onError(e);
    }

    /**
     * 此方法仅供JUnit测试使用，用于清除默认异常消费者，执行代码，
     * 然后重新设置默认消费者
     *
     * @param runnable 执行的操作
     * @see #setIgnore()
     * @see #setPrintStackTrace()
     */
    public static void temporaryIgnore(Runnable runnable) {
        setIgnore();
        runnable.run();
        setPrintStackTrace();
    }

    /**
     * 此方法仅供JUnit测试使用，用于清除默认异常消费者，执行代码，
     * 然后重新设置默认消费者
     *
     * @param supplier 执行的操作
     * @see #setIgnore()
     * @see #setPrintStackTrace()
     */
    public static <T> T temporarySupplier(Supplier<T> supplier) {
        setIgnore();
        T t = supplier.get();
        setPrintStackTrace();
        return t;
    }

    /**
     * 清除默认错误消费者
     */
    public static void setIgnore() {
        ERROR_THREAD_LOCAL.set(t -> {
        });
    }

    /**
     * 重置默认错误消费者
     */
    public static void setPrintStackTrace() {
        ERROR_THREAD_LOCAL.set(Throwable::printStackTrace);
    }
}
