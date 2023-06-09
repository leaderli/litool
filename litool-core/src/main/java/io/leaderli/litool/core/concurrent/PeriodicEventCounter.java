package io.leaderli.litool.core.concurrent;


import io.leaderli.litool.core.collection.LiFixedSizeSet;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 连续触发事件多次后执行操作
 */
public class PeriodicEventCounter {

    private final LiFixedSizeSet<Long> fixedSet;
    private final Runnable trigger;
    private final long nanoPeriod;

    /**
     * @param count   触发告警的次数
     * @param trigger 触发告警的回调函数
     * @param millis  触发告警的时间间隔
     */
    public PeriodicEventCounter(int count, Runnable trigger, long millis) {
        this.fixedSet = new LiFixedSizeSet<>(count);
        this.trigger = trigger;
        this.nanoPeriod = TimeUnit.MILLISECONDS.toNanos(millis);
    }

    /**
     * 计数，当连续时间内，计数达到指定此次时，则触发函数执行
     */
    public void count() {
        fixedSet.add(System.nanoTime());
        checkTrigger();
    }

    /**
     * 检测是否已经达到告警标准
     */
    public void checkTrigger() {
        List<Long> list = fixedSet.toList();
        if (list.size() == fixedSet.size) {
            long elapse = System.nanoTime() - list.get(fixedSet.size - 1);
            if (elapse < nanoPeriod) {
                trigger.run();
            }
        }

    }

}
