package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.executor.BaseElementExecutor;
import io.leaderli.litool.runner.executor.ElementExecutor;

public abstract class BaseElement<S extends SaxBean & ElementExecutor<S, E>, E extends BaseElementExecutor<S>> extends SaxBean implements ElementExecutor<S, E> {


    public BaseElement(String tag) {
        super(tag);
    }
}
