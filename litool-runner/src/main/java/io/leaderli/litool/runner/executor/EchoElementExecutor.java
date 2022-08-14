package io.leaderli.litool.runner.executor;

import io.leaderli.litool.core.event.LiEventObject;
import io.leaderli.litool.core.text.StrSubstitution;
import io.leaderli.litool.runner.Context;
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
//        Object[] objects = Lira.of(element.getExpressionList().getExpressionList())
//                .map(context::getExpressionValue)
//                .toArray();
//        String message = String.format(element.getValue(), objects);
        context.publishEvent(new EchoEvent(message));
    }

    public static final class EchoEvent extends LiEventObject<String> {

        public EchoEvent(String source) {
            super(source);
        }
    }
}
