package io.leaderli.litool.core.util;

import io.leaderli.litool.core.text.StringUtils;

import java.util.concurrent.TimeUnit;

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
        sleep(1, () -> System.out.println(StringUtils.join0(" ", msgs)));
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

    public static void printStack(int length) {

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = 2; i < length + 2; i++) {


            System.out.println(stackTrace[i]);
        }
    }
}
