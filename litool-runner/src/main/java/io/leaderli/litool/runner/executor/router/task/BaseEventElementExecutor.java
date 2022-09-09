package io.leaderli.litool.runner.executor.router.task;

import io.leaderli.litool.core.event.LiEventObject;
import io.leaderli.litool.core.text.StrSubstitution;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.executor.BaseElementExecutor;
import io.leaderli.litool.runner.util.ExpressionUtil;
import io.leaderli.litool.runner.xml.router.task.BaseEventElement;

/**
 * @author leaderli
 * @since 2022/8/14
 */
public abstract class BaseEventElementExecutor<B extends BaseEventElement<B, ?, E>, E extends LiEventObject<?>> extends BaseElementExecutor<B> {
    protected BaseEventElementExecutor(B element) {
        super(element);
    }

    @Override
    public final void execute(Context context) {


        String message = StrSubstitution.format(element.getLongExpression().getExpr()
                , expr -> ExpressionUtil.getExpression(expr).apply(context) + "");
        LiEventObject<?> e = newEvent(message);
        context.publishEvent(e);

    }

    public abstract E newEvent(String message);

}
