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
     * sleep some millis
     *
     * @param millis millis of {@link  TimeUnit#SECONDS}
     */
    public static void sleep(long millis) {
        sleep(TimeUnit.MILLISECONDS, millis);
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

    public static void sleep1s(Object... msgs) {
        sleep(1, () -> System.out.println(StringUtils.join(" ", msgs)));
    }

    /**
     * sleep some millis
     *
     * @param millis   millis of {@link  TimeUnit#SECONDS}
     * @param runnable execute after sleep
     */
    public static void sleep(long millis, Runnable runnable) {
        sleep(TimeUnit.MILLISECONDS, millis);
        runnable.run();
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
     * @param timeUnit a timeUnit
     * @param timeout  time timeout
     * @param <T>      the type of supplier
     * @param supplier provide a value after sleep
     * @return sleep a while and return a value
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

    @SuppressWarnings("java:S2142")
    public static void join() {
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
