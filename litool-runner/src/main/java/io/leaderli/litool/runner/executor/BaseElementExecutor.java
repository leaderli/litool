package io.leaderli.litool.runner.executor;

import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.ContextVisitor;

/**
 * @author leaderli
 * @since 2022/8/9 5:02 PM
 */
public abstract class BaseElementExecutor<S extends SaxBean> implements ContextVisitor {
    public final S element;

    public BaseElementExecutor(S element) {
        this.element = element;
    }
}
