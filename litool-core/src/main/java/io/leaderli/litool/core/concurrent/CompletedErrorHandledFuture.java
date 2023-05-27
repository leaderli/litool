package io.leaderli.litool.core.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * 一个已经包含值的{@link  ErrorHandledFuture}实现类
 *
 * @param <V> 值的 类型
 * @author leaderli
 * @since 2022/9/24
 */
public class CompletedErrorHandledFuture<V> implements ErrorHandledFuture<V> {
    private final V result;
    private final Throwable throwable;

    public CompletedErrorHandledFuture(V result, Throwable throwable) {
        this.result = result;
        this.throwable = throwable;
    }


    public static <V> CompletedErrorHandledFuture<V> withResult(V result, Throwable exc) {
        return new CompletedErrorHandledFuture<>(result, exc);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return true;
    }


    @Override
    public V get() {
        if (throwable == null) {
            return result;
        }
        throw new RuntimeException(throwable);
    }

    @Override
    public V get(long timeout, TimeUnit unit) {

        if (throwable == null) {
            return result;
        }
        throw new RuntimeException(throwable);
    }
}
