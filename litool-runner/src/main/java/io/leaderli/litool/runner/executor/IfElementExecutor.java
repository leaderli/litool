package io.leaderli.litool.runner.executor;

import io.leaderli.litool.core.collection.ImmutableList;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.TempNameEnum;
import io.leaderli.litool.runner.constant.UnitStateConstant;
import io.leaderli.litool.runner.xml.router.task.IfElement;

public class IfElementExecutor extends BaseElementExecutor<IfElement> {
    private ImmutableList<BaseElementExecutor<?>> executors;

    public IfElementExecutor(IfElement element) {
        super(element);
        init();
    }

    @SuppressWarnings("unchecked")
    private void init() {
        Lira<BaseElementExecutor<?>> map = element.getTaskList().lira().map(ElementExecutor::executor);
        executors = ImmutableList.of(map);
    }

    @Override
    public void visit(Context context) {
        Expression cond = element.getCond();
        Boolean expressionValue = (Boolean) context.getExpressionValue(cond);
        if (expressionValue) {
            for (BaseElementExecutor<?> executor : executors) {
                Integer unitState = context.getTemp(TempNameEnum.unit_state.name());
                if (unitState == UnitStateConstant.INTERRUPT) {
                    break;
                }
                if (executor instanceof GotoElementExecutor) {
                    context.setTemp(TempNameEnum.unit_state.name(), UnitStateConstant.INTERRUPT);
                }
                executor.visit(context);
            }
        }
    }

}
