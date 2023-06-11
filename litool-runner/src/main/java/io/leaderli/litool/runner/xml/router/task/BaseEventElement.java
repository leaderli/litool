package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.core.event.LiEventObject;
import io.leaderli.litool.dom.sax.BodyEvent;
import io.leaderli.litool.runner.LongExpression;
import io.leaderli.litool.runner.executor.router.task.BaseEventElementExecutor;

/**
 * @param <B>
 * @param <EXE>
 * @param <E>
 */
public abstract class BaseEventElement<B extends BaseEventElement<B, EXE, E>, EXE extends BaseEventElementExecutor<B,
        E>, E extends LiEventObject<?>> extends BaseElement<B, EXE> {


    protected LongExpression longExpression;

    protected BaseEventElement(String tag) {
        super(tag);
    }

    /**
     * @return 获取表达式
     */
    public LongExpression getLongExpression() {
        return longExpression;
    }

    /**
     * @param longExpression 表达式
     */
    public void setLongExpression(LongExpression longExpression) {
        this.longExpression = longExpression;
    }

    @Override
    public void body(BodyEvent bodyEvent) {
        String message = bodyEvent.description();
        this.longExpression = new LongExpression(message);
    }
}
