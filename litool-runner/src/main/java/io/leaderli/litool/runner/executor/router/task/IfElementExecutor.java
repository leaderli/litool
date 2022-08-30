package io.leaderli.litool.runner.executor.router.task;

import io.leaderli.litool.core.collection.ImmutableList;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.ContextVisitor;
import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.Interrupt;
import io.leaderli.litool.runner.executor.BaseElementExecutor;
import io.leaderli.litool.runner.executor.ElementExecutor;
import io.leaderli.litool.runner.xml.router.task.IfElement;

import java.util.List;

public class IfElementExecutor extends BaseElementExecutor<IfElement> {
    private ImmutableList<ContextVisitor> executors;

    public IfElementExecutor(IfElement element) {
        super(element);
        init();
    }

    private void init() {
        Lira<ContextVisitor> map = element.getTaskList().lira().map(ElementExecutor::executor);
        executors = ImmutableList.of(map);
    }

    @Override
    public void execute(Context context) {
        Expression cond = element.getCond();
        boolean expressionValue = (Boolean) context.getExpressionValue(cond);

        if (!expressionValue) {
            context.interrupt.set(Interrupt.BREAK_LOOP);
        }
    }

    @Override
    public boolean notify(Context context) {
        if (context.interrupt.have(Interrupt.BREAK_LOOP)) {
            context.interrupt.disable(Interrupt.BREAK_LOOP);
        }
        return false;
    }

    @Override
    public List<ContextVisitor> visit() {
        return executors.toList();
    }
}
