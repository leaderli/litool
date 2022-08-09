package io.leaderli.litool.runner.executor;

/**
 * @author leaderli
 * @since 2022/8/9 4:49 PM
 */
public interface ElementExecutor<T extends BaseElementExecutor<?>> {

    T executor();
}
