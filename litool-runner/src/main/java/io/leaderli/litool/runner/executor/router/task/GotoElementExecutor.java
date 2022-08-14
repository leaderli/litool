package io.leaderli.litool.runner.executor.router.task;

import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.TempNameEnum;
import io.leaderli.litool.runner.constant.UnitStateConstant;
import io.leaderli.litool.runner.executor.BaseElementExecutor;
import io.leaderli.litool.runner.xml.router.task.GotoElement;

public class GotoElementExecutor extends BaseElementExecutor<GotoElement> {
    public GotoElementExecutor(GotoElement element) {
        super(element);
    }

    @Override
    public void visit(Context context) {
        context.setTemp(TempNameEnum.sequence_name.name(), element.getNext().next);
        context.setTemp(TempNameEnum.unit_state.name(), UnitStateConstant.INTERRUPT);
    }
}
