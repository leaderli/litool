package io.leaderli.litool.runner.executor.router.task;

import io.leaderli.litool.core.event.LiEventObject;
import io.leaderli.litool.core.text.StrSubstitution;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.executor.BaseElementExecutor;
import io.leaderli.litool.runner.util.ExpressionUtil;
import io.leaderli.litool.runner.xml.router.task.BaseEventElement;

import java.util.function.BiFunction;

/**
 * @param <B> 事件元素类型
 * @param <E> 事件信息类型
 * @author leaderli
 * @since 2022/8/14
 */
public abstract class BaseEventElementExecutor<B extends BaseEventElement<B, ?, E>, E extends LiEventObject<?>> extends BaseElementExecutor<B> {
    protected BaseEventElementExecutor(B element) {
        super(element);
    }

    @Override
    public final void execute(Context context) {


        BiFunction<String, String, Object> replacer = (expr, def) -> String.valueOf(ExpressionUtil.getExpression(expr).apply(context));
        String expr = element.getLongExpression().getExpr();
        String message = StrSubstitution.parse(expr, "{", "}", replacer);
        LiEventObject<?> e = newEvent(message);
        context.publishEvent(e);

    }

    /**
     * @param message -
     * @return -
     */
    public abstract E newEvent(String message);

}
