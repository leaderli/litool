package io.leaderli.litool.runner.executor.router;

import io.leaderli.litool.core.collection.ImmutableList;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.ContextVisitor;
import io.leaderli.litool.runner.Interrupt;
import io.leaderli.litool.runner.event.UnitErrorEvent;
import io.leaderli.litool.runner.executor.BaseElementExecutor;
import io.leaderli.litool.runner.xml.router.SequenceElement;
import io.leaderli.litool.runner.xml.router.UnitElement;

import java.util.List;

public class SequenceElementExecutor extends BaseElementExecutor<SequenceElement> {

    private ImmutableList<ContextVisitor> unitExecutors;

    public SequenceElementExecutor(SequenceElement element) {
        super(element);
        init();
    }

    private void init() {
        unitExecutors = ImmutableList.of(element.getUnitList().lira().map(UnitElement::executor));
    }

    @Override
    public boolean notify(Context context) {
        if (context.interrupt.have(Interrupt.ERROR)) {
            context.publishEvent(new UnitErrorEvent(element.getId(), (Throwable) context.interruptObj));
            context.interrupt.disable(Interrupt.ERROR);
            return true;
        }
        return false;
    }

    @Override
    public List<ContextVisitor> visit() {
        return unitExecutors.toList();
    }
}
