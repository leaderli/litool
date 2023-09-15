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
    private final Map<T, R> result;
    private long lastExecutedAt = 0;

    /**
     * 5000ms
     *
     * @see #PeriodFunction(long, Function, Map, ExecutorService)
     */
    public PeriodFunction(Function<T, R> function, ExecutorService executorService) {
        this(5000, function, new HashMap<>(), executorService);
    }

    /**
     * 5000ms
     *
     * @see #PeriodFunction(long, Function, Map, ExecutorService)
     */
    public PeriodFunction(Function<T, R> function, Map<T, R> result, ExecutorService executorService) {
        this(5000, function, result, executorService);
    }

    /**
     * @param check_millis    检测周期
     * @param function        更新函数
     * @param initResult      初始值
     * @param executorService 线程
     */
    public PeriodFunction(long check_millis, Function<T, R> function, Map<T, R> initResult, ExecutorService executorService) {
        this.check_millis = check_millis;
        this.function = function;
        this.result = initResult;
        this.executorService = executorService;
    }

    private void trigger(T key) {
        long now = System.currentTimeMillis();
        if (now - lastExecutedAt > check_millis) {
            if (result.containsKey(key)) {
                //异步线程执行，不需要考虑异常情况
                executorService.submit(() -> {
                    R value = function.apply(key);
                    if (value != null) {
                        result.put(key, value);
                    }
                });

            } else {
                R value = function.apply(key);
                LiAssertUtil.assertNotNull(value, "can not get a value by key " + key);
                result.put(key, value);
            }
            lastExecutedAt = now;
        }
    }

    @Override
    public R apply(T t) {
        trigger(t);
        return result.get(t);
    }
}
