package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.dom.sax.BodyEvent;
import io.leaderli.litool.runner.LongExpression;
import io.leaderli.litool.runner.event.EchoEvent;
import io.leaderli.litool.runner.executor.EchoElementExecutor;

public class EchoElement extends TaskElement<EchoElement, EchoElementExecutor> {

    private int level = EchoEvent.DEBUG;

    private LongExpression longExpression;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public LongExpression getLongExpression() {
        return longExpression;
    }

    public void setLongExpression(LongExpression longExpression) {
        this.longExpression = longExpression;
    }


    @Override
    public void body(BodyEvent bodyEvent) {
        String message = bodyEvent.description();
        this.longExpression = new LongExpression(message);
    }


    @Override
    public String tag() {
        return "echo";
    }


}
