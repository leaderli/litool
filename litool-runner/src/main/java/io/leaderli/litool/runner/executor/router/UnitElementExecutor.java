package io.leaderli.litool.runner.executor.router;

import io.leaderli.litool.core.collection.ImmutableList;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.ContextVisitor;
import io.leaderli.litool.runner.Interrupt;
import io.leaderli.litool.runner.executor.BaseElementExecutor;
import io.leaderli.litool.runner.executor.ElementExecutor;
import io.leaderli.litool.runner.xml.router.UnitElement;

import java.util.List;

public class UnitElementExecutor extends BaseElementExecutor<UnitElement> {

    private ImmutableList<ContextVisitor> executors;

    public UnitElementExecutor(UnitElement element) {
        super(element);
        init();
    }

    private void init() {
        Lira<ContextVisitor> map = element.getTaskList().lira()
                .map(ElementExecutor::executor)
                .map(UnitElementExecutorWrapper::new);
        executors = ImmutableList.of(map);
    }

    @Override
    public List<ContextVisitor> visit() {
        return executors.copy();
    }



    private static class UnitElementExecutorWrapper extends ContextVisitor {

        private final ContextVisitor contextVisitor;

        private UnitElementExecutorWrapper(ContextVisitor contextVisitor) {
            this.contextVisitor = contextVisitor;
        }

        @Override
        protected void execute(Context context) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void visit(Context context) {
            try {
                contextVisitor.visit(context);
            } catch (Throwable throwable) {
                context.interrupt.set(Interrupt.ERROR);
                context.interruptObj = throwable;
            }
        }
    }
}
