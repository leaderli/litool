package io.leaderli.litool.core.lang;

/**
 * a runnable proxy that provide a one-time execution of  the actual task
 *
 * @author leaderli
 * @since 2022/9/2 8:38 AM
 */
public class DisposableRunnableProxy implements Runnable {
    private final Runnable actual;
    private boolean executed;

    private DisposableRunnableProxy(Runnable actual) {
        this.actual = actual;
    }

    public static DisposableRunnableProxy of(Runnable runnable) {
        return new DisposableRunnableProxy(runnable);
    }

    @Override
    public final void run() {
        if (!executed) {
            executed = true;
            actual.run();
        }
    }


}
