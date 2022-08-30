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
     * @param duration duration of {@link  TimeUnit#SECONDS}
     */
    public static void sleep(long duration) {
        sleep(TimeUnit.SECONDS, duration);
    }

    /**
     * sleep a while
     *
     * @param timeUnit a timeUnit
     * @param duration time duration
     */
    @SuppressWarnings("java:S2142")
    public static void sleep(TimeUnit timeUnit, long duration) {
        try {
            timeUnit.sleep(duration);
        } catch (InterruptedException ignore) {
            // ignore
        }
    }
}
