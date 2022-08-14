package io.leaderli.litool.runner.executor;

import io.leaderli.litool.core.event.LiEventObject;
import io.leaderli.litool.core.text.StrSubstitution;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.util.ExpressionUtil;
import io.leaderli.litool.runner.xml.router.task.BaseEventElement;

/**
 * @author leaderli
 * @since 2022/8/14
 */
public abstract class EventElementExecutor<B extends BaseEventElement<B, ?, E>, E extends LiEventObject<?>> extends BaseElementExecutor<B> {
    public EventElementExecutor(B element) {
        super(element);
    }

    @Override
    public final void visit(Context context) {


        String message = StrSubstitution.replace(element.getLongExpression().getExpr()
                , expr -> ExpressionUtil.getExpression(expr).apply(context) + "");
        LiEventObject<?> e = newEvent(message);
        context.publishEvent(e);

    }

    public abstract E newEvent(String message);

}
