package io.leaderli.litool.core.util;

import io.leaderli.litool.core.text.StringUtils;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author leaderli
 * @since 2022/8/30
 */
public class ThreadUtil {


    /**
     * sleep some second
     *
     * @param second second of {@link  TimeUnit#SECONDS}
     */
    public static void sleep(long second) {
        sleep(TimeUnit.SECONDS, second);
    }

    public static void sleep1s(Object... msgs) {
        sleep(1, () -> System.out.println(StringUtils.join(" ", msgs)));
    }

    /**
     * sleep some second
     *
     * @param second   second of {@link  TimeUnit#SECONDS}
     * @param runnable execute after sleep
     */
    public static void sleep(long second, Runnable runnable) {
        sleep(TimeUnit.SECONDS, second);
        runnable.run();
    }

    /**
     * sleep a while
     *
     * @param timeUnit a timeUnit
     * @param timeout  time timeout
     */
    @SuppressWarnings("java:S2142")
    public static void sleep(TimeUnit timeUnit, long timeout) {
        try {
            timeUnit.sleep(timeout);
        } catch (InterruptedException ignore) {
            // ignore
        }
    }

    /**
     * sleep a while
     *
     * @param timeUnit a timeUnit
     * @param timeout  time timeout
     * @param runnable execute after sleep
     */
    @SuppressWarnings("java:S2142")
    public static void sleep(TimeUnit timeUnit, long timeout, Runnable runnable) {
        try {
            timeUnit.sleep(timeout);
            runnable.run();
        } catch (InterruptedException ignore) {
            // ignore
        }
    }

    /**
     * sleep a while and return a value
     *
     * @param timeUnit a timeUnit
     * @param timeout  time timeout
     * @param supplier provide a value after sleep
     */
    public static <T> T delay(TimeUnit timeUnit, long timeout, Supplier<T> supplier) {

        sleep(timeUnit, timeout);
        return supplier.get();
    }

    /**
     * print  current thread stack, start with 3rd stack
     *
     * @param width the print stack with
     */
    public static void printStack(int width) {

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = 2; i < width + 2; i++) {
            System.out.println(stackTrace[i]);
        }
    }
}
