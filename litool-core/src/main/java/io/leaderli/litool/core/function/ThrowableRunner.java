package io.leaderli.litool.core.function;

/**
 * @author leaderli
 * @since 2022/6/16
 */
@FunctionalInterface
public interface ThrowableRunner {

    void run() throws Throwable;
}
