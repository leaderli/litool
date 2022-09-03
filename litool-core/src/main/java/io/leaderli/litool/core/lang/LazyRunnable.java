package io.leaderli.litool.core.lang;

/**
 * @author leaderli
 * @since 2022/9/2 8:38 AM
 */
public class LazyRunnable implements Runnable {
    private final Runnable runnable;
    private boolean executed;

    private LazyRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public static LazyRunnable of(Runnable runnable) {
        return new LazyRunnable(runnable);
    }

    @Override
    public final void run() {
        if (!executed) {
            executed = true;
            runnable.run();
        }
    }


}
