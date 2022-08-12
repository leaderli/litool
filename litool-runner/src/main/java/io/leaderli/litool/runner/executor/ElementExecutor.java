package io.leaderli.litool.runner.executor;

import io.leaderli.litool.dom.sax.SaxBean;

/**
 * @author leaderli
 * @since 2022/8/9 4:49 PM
 */
public interface ElementExecutor<R extends SaxBean & ElementExecutor<R, T>, T extends BaseElementExecutor<R>> {

    T executor();
}
