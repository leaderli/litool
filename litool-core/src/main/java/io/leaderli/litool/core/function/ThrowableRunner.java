package io.leaderli.litool.core.function;

/**
 * 一个声明异常标识的 {@link  Runnable}
 *
 * @author leaderli
 * @since 2022/6/16
 */

@FunctionalInterface
public interface ThrowableRunner {
    /**
     * Performs an action
     *
     * @throws Throwable error
     */
    void run() throws Throwable;
}
