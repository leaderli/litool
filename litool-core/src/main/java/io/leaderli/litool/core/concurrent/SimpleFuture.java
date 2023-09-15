package io.leaderli.litool.core.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author leaderli
 * @since 2023/9/15 11:38 AM
 */
public class SimpleFuture<T> implements Future<T> {
    private volatile boolean cancelled;
    private volatile boolean done;
    private T result;

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (done) {
            return false;
        }
        cancelled = true;
        done = true;
        return true;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        synchronized (this) {
            while (!done) {
                wait();
            }
        }
        return result;
    }

    public T get(long timeoutMillis) throws InterruptedException, ExecutionException, TimeoutException {
        long endTime = System.currentTimeMillis() + timeoutMillis;

        synchronized (this) {
            while (!done) {
                long timeLeft = endTime - System.currentTimeMillis();
                if (timeLeft <= 0) {
                    throw new TimeoutException();
                }
                wait(timeLeft);
            }
        }
        return result;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return get(unit.toMillis(timeout));
    }

    public void setResult(T result) {
        synchronized (this) {
            if (!done && !cancelled) {
                this.result = result;
                done = true;
                notifyAll();
            }
        }
    }

}
