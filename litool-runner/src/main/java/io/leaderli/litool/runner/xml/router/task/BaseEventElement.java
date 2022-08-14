package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.core.event.LiEventObject;
import io.leaderli.litool.dom.sax.BodyEvent;
import io.leaderli.litool.runner.LongExpression;
import io.leaderli.litool.runner.executor.EventElementExecutor;

public abstract class BaseEventElement<EL extends BaseEventElement<EL, EXE, EV>, EXE extends EventElementExecutor<EL, EV>, EV extends LiEventObject<?>> extends BaseElement<EL, EXE> {


    protected LongExpression longExpression;

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
}
