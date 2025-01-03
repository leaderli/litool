package io.leaderli.litool.core.concurrent;

import java.util.concurrent.*;

/**
 * @author leaderli
 * @since 2023/9/15 11:38 AM
 */
public class SimpleFuture<T> implements Future<T> {
    /**
     * 任务是否取消了
     */
    private volatile boolean cancelled;
    /**
     * 任务是否结束
     */
    private volatile boolean done;
    /**
     * 执行结果，当没有值时可能为null
     */
    private volatile T result;
    /**
     * 任务执行时，可能存在的异常
     */
    private volatile Exception exception;

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

    public boolean hasException() {
        return exception != null;
    }

    public Exception getException() {
        if (isDone()) {
            return exception;
        }
        return new IllegalStateException("task not finished");
    }

    @Override
    public T get() throws InterruptedException {
        synchronized (this) {
            while (!done) {
                wait();
            }
        }
        return result;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
        return get(unit.toMillis(timeout));
    }

    public T get(long timeoutMillis) throws InterruptedException, TimeoutException {
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

    public void setResult(T result) {
        synchronized (this) {
            if (!done && !cancelled) {
                this.result = result;
                done = true;
                notifyAll();
            }
        }
    }

    public void setException(Exception exception) {
        synchronized (this) {
            if (!done && !cancelled) {
                this.exception = exception;
                done = true;
                notifyAll();
            }
        }
    }

    public void reset() {
        synchronized (this) {
            done = false;
            cancelled = false;
            result = null;
            exception = null;
            notifyAll();
        }
    }


    public void submit(Callable<T> supplier) {
        new Thread(() -> {
            try {
                setResult(supplier.call());
            } catch (Exception e) {
                setException(e);
            }
        }).start();
    }


    public void submit(ExecutorService executorService, Callable<T> supplier) {
        try {

            executorService.submit(() -> {
                try {
                    setResult(supplier.call());
                } catch (Exception e) {
                    setException(e);
                }
            });
        } catch (RejectedExecutionException e) {
            submit(supplier);
        }
    }

}
