package io.leaderli.litool.core.util;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * ThreadUtil类提供了一些线程相关的工具方法。
 * 包括线程休眠、延时获取返回值、打印线程堆栈等功能。
 */
public class ThreadUtil {


    /**
     * 休眠一定时间，单位为毫秒。
     *
     * @param millis 休眠的毫秒数
     */
    public static void sleep(long millis) {
        sleep(TimeUnit.MILLISECONDS, millis);
    }

    /**
     * 休眠一定时间
     *
     * @param timeUnit 时间单位
     * @param timeout  休眠的时间
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
     * 休眠一定时间，并在休眠结束后执行runnable。
     *
     * @param millis   休眠的毫秒数
     * @param runnable 休眠结束后执行的操作
     */
    public static void sleep(long millis, Runnable runnable) {
        sleep(TimeUnit.MILLISECONDS, millis);
        runnable.run();
    }


    /**
     * 休眠一定时间，并在休眠结束后执行runnable。
     *
     * @param timeUnit 时间单位
     * @param timeout  休眠的时间
     * @param runnable 休眠结束后执行的操作
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
     * 延时一定时间，并返回结果。
     *
     * @param timeUnit 时间单位
     * @param timeout  休眠的时间
     * @param supplier 提供返回值的方法
     * @param <T>      返回值类型
     * @return 休眠一定时间后的返回值
     */
    public static <T> T delay(TimeUnit timeUnit, long timeout, Supplier<T> supplier) {

        sleep(timeUnit, timeout);
        return supplier.get();
    }

    /**
     * 打印当前线程的堆栈信息。
     *
     * @param start 堆栈信息的起始位置
     */
    public static void printStack(int start) {

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = 2; i < start + 2 && i < stackTrace.length; i++) {
            System.out.println(stackTrace[i]);
        }
    }

    /**
     * 返回当前线程的堆栈信息。
     *
     * @param start 堆栈信息的起始位置
     */
    public static String stackTrace(int start) {

        StringBuilder sb = new StringBuilder();
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = 2; i < start + 2 && i < stackTrace.length; i++) {
            sb.append(stackTrace[i]).append(System.lineSeparator());
        }
        return sb.toString();
    }

    /**
     * 等待当前线程结束。
     *
     * @throws RuntimeException 如果当前线程被中断，抛出RuntimeException。
     */
    @SuppressWarnings("java:S2142")
    public static void join() {
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
