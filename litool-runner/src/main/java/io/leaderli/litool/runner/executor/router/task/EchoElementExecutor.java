package io.leaderli.litool.runner.executor.router.task;

import io.leaderli.litool.runner.event.EchoEvent;
import io.leaderli.litool.runner.xml.router.task.EchoElement;

public class EchoElementExecutor extends BaseEventElementExecutor<EchoElement, EchoEvent> {
    public EchoElementExecutor(EchoElement element) {
        super(element);
    }


    @Override
    public EchoEvent newEvent(String message) {
        return new EchoEvent(element.getLevel(), message);
    }

}
