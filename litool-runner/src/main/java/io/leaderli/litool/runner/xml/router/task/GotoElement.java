package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.runner.executor.router.task.GotoElementExecutor;

public class GotoElement extends BaseElement<GotoElement, GotoElementExecutor> {

    private GotoDestination next;

    public GotoElement() {
        super("goto");
    }

    public GotoDestination getNext() {
        return next;
    }

    public void setNext(GotoDestination next) {
        this.next = next;
    }

    @Override
    public GotoElementExecutor executor() {
        return new GotoElementExecutor(this);
    }

}
