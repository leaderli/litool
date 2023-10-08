package io.leaderli.litool.core.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

/**
 * 一个定期更新值的提供者,更新操作通过另外的线程执行
 *
 * @param <T>
 */
public class PeriodSupplier<T> implements Supplier<T> {

    private final long check_millis;
    private final Supplier<T> supplier;
    private final ExecutorService executorService;
    private volatile T result;
    private volatile long lastExecutedAt = 0;
    private volatile boolean update = false;
    private final boolean allowNullValue;

    /**
     * check_millis= 5000
     * resetNow = false
     * allowNullValue = false
     *
     * @param supplier        更新函数
     * @param initResult      初始值
     * @param executorService 线程
     * @see #PeriodSupplier(long, Supplier, Object, ExecutorService, boolean, boolean)
     */
    public PeriodSupplier(Supplier<T> supplier, T initResult, ExecutorService executorService) {
        this(5000, supplier, initResult, executorService, false, false);
    }


    /**
     * @param check_millis    检测周期
     * @param supplier        更新函数
     * @param initResult      初始值
     * @param executorService 线程
     * @param allowNullValue  是否允许空值
     */
    public PeriodSupplier(long check_millis, Supplier<T> supplier, T initResult, ExecutorService executorService, boolean allowNullValue, boolean resetNow) {
        this.check_millis = check_millis;
        this.supplier = supplier;
        this.result = initResult;
        this.executorService = executorService;
        this.allowNullValue = allowNullValue;
        if (resetNow) {
            this.lastExecutedAt = System.currentTimeMillis();
        }
    }

    @Override
    public T get() {
        try {
            trigger();
        } catch (InterruptedException ignored) {
        }
        return result;
    }

    private void trigger() throws InterruptedException {
        long now = System.currentTimeMillis();
        if (now - lastExecutedAt < check_millis) {
            return;
        }
        synchronized (this) {
            if (now - lastExecutedAt < check_millis) {
                return;
            }

            // 其他线程等待更新结果
            if (update) {
                wait(check_millis);
                return;
            }


            update = true;
            executorService.submit(() -> {
                synchronized (this) {
                    try {

                        T temp = supplier.get();
                        if (allowNullValue || temp != null) {
                            result = temp;
                            lastExecutedAt = now;
                        }
                        // 无论更新失败与否等唤醒其他线程
                        notifyAll();
                    } finally {
                        // 防止异常的时候，更新标记未重置
                        update = false;
                    }
                }

            });
            // 当前线程等待结果返回
            if (update) {
                wait(check_millis);
            }
        }
    }
}
