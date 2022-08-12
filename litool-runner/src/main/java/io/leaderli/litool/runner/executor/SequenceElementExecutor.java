package io.leaderli.litool.runner.executor;

import io.leaderli.litool.core.collection.ImmutableList;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.TempNameEnum;
import io.leaderli.litool.runner.constant.UnitStateConstant;
import io.leaderli.litool.runner.xml.router.SequenceElement;
import io.leaderli.litool.runner.xml.router.UnitElement;

import java.util.List;

public class SequenceElementExecutor extends BaseElementExecutor<SequenceElement> {

    private ImmutableList<UnitElementExecutor> unitExecutors;

    public SequenceElementExecutor(SequenceElement element) {
        super(element);
        init();
    }

    private void init() {
        unitExecutors = ImmutableList.of(element.getUnitList().lira().map(UnitElement::executor));
    }

    @Override
    public void visit(Context context) {
        for (UnitElementExecutor unitExecutor : unitExecutors) {
            Integer unitState = context.getTemp(TempNameEnum.unit_state.name());
            if (unitState == UnitStateConstant.INTERRUPT) {
                break;
            }
            unitExecutor.visit(context);
        }
    }
}
