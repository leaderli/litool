package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.dom.sax.BodyEvent;
import io.leaderli.litool.runner.LongExpression;
import io.leaderli.litool.runner.executor.EchoElementExecutor;

public class EchoElement extends TaskElement<EchoElement, EchoElementExecutor> {
    private static final String EXPRESSION_PATTERN = "\\{[^}]*}";

    private LongExpression longExpression;


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
