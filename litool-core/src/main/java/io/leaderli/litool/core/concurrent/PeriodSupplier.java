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
    T result;
    private long lastExecutedAt = 0;

    /**
     * 5000ms
     *
     * @see #PeriodSupplier(long, Supplier, Object, ExecutorService)
     */
    public PeriodSupplier(Supplier<T> supplier, T result, ExecutorService executorService) {
        this(5000, supplier, result, executorService);
    }

    /**
     * @param check_millis    检测周期
     * @param supplier        更新函数
     * @param initResult      初始值
     * @param executorService 线程
     */
    public PeriodSupplier(long check_millis, Supplier<T> supplier, T initResult, ExecutorService executorService) {
        this.check_millis = check_millis;
        this.supplier = supplier;
        this.result = initResult;
        this.executorService = executorService;
    }

    @Override
    public T get() {
        trigger();
        return result;
    }

    private void trigger() {
        long now = System.currentTimeMillis();
        if (now - lastExecutedAt > check_millis) {
            //异步线程执行，不需要考虑异常情况
            executorService.submit(() -> {
                T temp = supplier.get();
                if (temp != null) {
                    result = temp;
                }
            });
            lastExecutedAt = now;
        }
    }
}
