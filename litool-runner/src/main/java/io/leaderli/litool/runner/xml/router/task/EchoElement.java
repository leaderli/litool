package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.runner.event.EchoEvent;
import io.leaderli.litool.runner.executor.router.task.EchoElementExecutor;

public class EchoElement extends BaseEventElement<EchoElement, EchoElementExecutor, EchoEvent> {

    private int level = EchoEvent.DEBUG;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }


    @Override
    public String tag() {
        return "echo";
    }


}
