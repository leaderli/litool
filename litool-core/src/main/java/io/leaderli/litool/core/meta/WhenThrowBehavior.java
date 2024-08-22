package io.leaderli.litool.core.meta;

import io.leaderli.litool.core.function.OnError;
import io.leaderli.litool.core.function.ThrowableFunction;

/**
 * litool工具类内部抛出异常时的默认行为
 */
public class WhenThrowBehavior {
    /**
     * 当发生异常时的默认消费者，适用于以下方法：
     *
     * @see Lino#mapIgnoreError(ThrowableFunction)
     * @see Lira#mapIgnoreError(ThrowableFunction)
     */
    @SuppressWarnings("all")
    public static OnError WHEN_THROW = OnError.PRINT_STACK;

    /**
     * 使用默认异常消费者接受一个异常
     *
     * @param e 异常
     */
    public static void whenThrow(Throwable e) {
        if (WHEN_THROW != null) {
            WHEN_THROW.onError(e);
        }
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
     * 清除默认错误消费者
     */
    public static void setIgnore() {
        WHEN_THROW = null;
    }

    /**
     * 重置默认错误消费者
     */
    public static void setPrintStackTrace() {
        WHEN_THROW = OnError.PRINT_STACK;
    }
}
