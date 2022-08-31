package io.leaderli.litool.core.util;

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
}
