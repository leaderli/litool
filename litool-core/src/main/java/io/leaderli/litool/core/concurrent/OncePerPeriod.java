package io.leaderli.litool.core.concurrent;

/**
 * @author leaderli
 * @since 2023/5/19 10:33 AM
 */
public class OncePerPeriod {


    private final long period;
    private long lastExecutedAt = 0;

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
    public boolean executeOnce(Runnable runnable, boolean sync) {
        long now = System.currentTimeMillis();
        if (now - lastExecutedAt > period) {
            if (sync) {

                synchronized (this) {
                    if (now - lastExecutedAt > period) {
                        runnable.run();
                        lastExecutedAt = now;
                        return true;
                    }
                }
            } else {
                runnable.run();
                lastExecutedAt = now;
                return true;
            }
        }
        return false;
    }

    /**
     * {@link  #period} 时间内最多执行一次，不考虑多线程的情况
     *
     * @param runnable 执行的任务
     * @see #executeOnce(Runnable, boolean)
     */
    public boolean executeOnce(Runnable runnable) {
        return executeOnce(runnable, false);
    }

    public boolean shouldExecute() {
        return shouldExecute(false);
    }

    public void setToNow() {
        this.lastExecutedAt = System.currentTimeMillis();
    }

    public boolean shouldExecute(boolean sync) {
        long now = System.currentTimeMillis();
        if (sync) {
            synchronized (this) {
                return now - lastExecutedAt > period;
            }
        } else {
            return now - lastExecutedAt > period;
        }
    }

    @Override
    public String toString() {
        return "OncePerPeriod{" +
                "period=" + period +
                ", lastExecutedAt=" + lastExecutedAt +
                '}';
    }
}
