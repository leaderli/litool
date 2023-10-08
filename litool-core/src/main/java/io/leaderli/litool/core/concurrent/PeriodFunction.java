package io.leaderli.litool.core.concurrent;

import io.leaderli.litool.core.exception.LiAssertUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

/**
 * 一个定期更新值的函数,更新操作通过另外的线程执行
 *
 * @param <T>
 * @param <R>
 */
public class PeriodFunction<T, R> implements Function<T, R> {

    private final long check_millis;
    private final Function<T, R> function;
    private final ExecutorService executorService;
    private final Map<T, PeriodSupplier<R>> periodSupplierMap = new HashMap<>();
    private final boolean allowNullValue;

    /**
     * 5000ms
     *
     * @see #PeriodFunction(long, Function, ExecutorService, boolean)
     */
    public PeriodFunction(Function<T, R> function, ExecutorService executorService) {
        this(5000, function, executorService, false);
    }


    /**
     * @param check_millis    检测周期
     * @param function        更新函数
     * @param executorService 线程
     */
    public PeriodFunction(long check_millis, Function<T, R> function, ExecutorService executorService, boolean allowNull) {
        this.check_millis = check_millis;
        this.function = function;
        this.executorService = executorService;
        this.allowNullValue = allowNull;
    }


    @Override
    public R apply(T t) {
        PeriodSupplier<R> periodSupplier = periodSupplierMap.get(t);

        if (periodSupplier != null) {
            return periodSupplier.get();
        }

        synchronized (periodSupplierMap) {
            R def = function.apply(t);
            if (!allowNullValue) {
                LiAssertUtil.assertNotNull(def, "error at init period supplier");
            }
            periodSupplierMap.put(t, new PeriodSupplier<>(check_millis, () -> function.apply(t), def, executorService, allowNullValue, true));
            return def;
        }
    }
}
