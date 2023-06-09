package io.leaderli.litool.core.concurrent;

/**
 * @author leaderli
 * @since 2023/5/19 10:33 AM
 */
public class OncePerPeriod {


    private long lastExecutedAt = 0;
    private final long period;

    /**
     * @param period 时间毫秒区间
     */
    public OncePerPeriod(long period) {
        this.period = period;
    }

    /**
     * {@link  #period} 时间内最多执行一次
     *
     * @param runnable 执行的任务
     * @param sync     是否加锁，保证在多线程的情况下也能满足条件
     */
    public void executeOnce(Runnable runnable, boolean sync) {
        long now = System.currentTimeMillis();
        if (now - lastExecutedAt > period) {
            if (sync) {

                synchronized (this) {
                    if (now - lastExecutedAt > period) {
                        runnable.run();
                        lastExecutedAt = now;
                    }
                }
            } else {
                runnable.run();
                lastExecutedAt = now;
            }
        }
    }

    /**
     * {@link  #period} 时间内最多执行一次，不考虑多线程的情况
     *
     * @param runnable 执行的任务
     * @see #executeOnce(Runnable, boolean)
     */
    public void executeOnce(Runnable runnable) {
        executeOnce(runnable, false);
    }

}
