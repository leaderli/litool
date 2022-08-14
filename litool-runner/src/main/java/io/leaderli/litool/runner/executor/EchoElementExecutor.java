package io.leaderli.litool.runner.executor;

import io.leaderli.litool.core.text.StrSubstitution;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.event.EchoEvent;
import io.leaderli.litool.runner.util.ExpressionUtil;
import io.leaderli.litool.runner.xml.router.task.EchoElement;

public class EchoElementExecutor extends BaseElementExecutor<EchoElement> {
    public EchoElementExecutor(EchoElement element) {
        super(element);
    }

    @Override
    public void visit(Context context) {


        String message = StrSubstitution.replace(element.getLongExpression().getExpr()
                , expr -> ExpressionUtil.getExpression(expr).apply(context) + "");
        context.publishEvent(new EchoEvent(element.getLevel(), message));
    }

}
