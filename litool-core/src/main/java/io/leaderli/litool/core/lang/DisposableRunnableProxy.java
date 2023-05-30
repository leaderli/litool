package io.leaderli.litool.core.lang;


/**
 * 仅执行一次实际函数的函数代理
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
