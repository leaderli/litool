package io.leaderli.litool.runner.executor;

import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.ContextVisitor;

/**
 * @author leaderli
 * @since 2022/8/9 5:02 PM
 */
public abstract class BaseElementExecutor<T extends SaxBean> implements ContextVisitor {
    protected final T element;

    public BaseElementExecutor(T element) {
        this.element = element;
    }
}
