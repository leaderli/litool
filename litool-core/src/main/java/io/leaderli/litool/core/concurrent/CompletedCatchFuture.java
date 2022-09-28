package io.leaderli.litool.core.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * a {@link  CatchFuture} implement that already contain the result
 *
 * @author leaderli
 * @since 2022/9/24
 */
public class CompletedCatchFuture<V> implements CatchFuture<V> {
    private final V result;
    private final Throwable exc;

    public CompletedCatchFuture(V result, Throwable exc) {
        this.result = result;
        this.exc = exc;
    }


    public static <V> CompletedCatchFuture<V> withResult(V result, Throwable exc) {
        return new CompletedCatchFuture<>(result, exc);
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
        if (exc != null) {
            throw new RuntimeException(exc);
        }
        return result;
    }

    @Override
    public V get(long timeout, TimeUnit unit) {
        if (unit == null) {
            throw new NullPointerException();
        }
        if (exc != null) {
            throw new RuntimeException(exc);
        }
        return result;
    }
}
