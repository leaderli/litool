package io.leaderli.litool.runner.executor.router;

import io.leaderli.litool.core.collection.ImmutableList;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.TempNameEnum;
import io.leaderli.litool.runner.constant.UnitStateConstant;
import io.leaderli.litool.runner.executor.BaseElementExecutor;
import io.leaderli.litool.runner.executor.ElementExecutor;
import io.leaderli.litool.runner.xml.router.UnitElement;

public class UnitElementExecutor extends BaseElementExecutor<UnitElement> {

    private ImmutableList<BaseElementExecutor<?>> executors;

    public UnitElementExecutor(UnitElement element) {
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
        try {
            for (BaseElementExecutor<?> executor : executors) {
                Integer unitState = context.getTemp(TempNameEnum.unit_state.name());
                if (unitState == UnitStateConstant.INTERRUPT) {
                    break;
                }
                executor.visit(context);
            }
        } catch (Exception e) {
            context.setTemp(TempNameEnum.unit_state.name(), UnitStateConstant.INTERRUPT);
        }
    }
}
