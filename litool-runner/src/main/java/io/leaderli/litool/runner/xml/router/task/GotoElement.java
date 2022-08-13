package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.runner.executor.GotoElementExecutor;

public class GotoElement extends TaskElement<GotoElement, GotoElementExecutor> {

    private GotoDestination next;

    @Override
    public String tag() {
        return "goto";
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