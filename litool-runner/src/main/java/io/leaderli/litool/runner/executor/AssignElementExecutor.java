package io.leaderli.litool.runner.executor;

import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.xml.router.task.AssignElement;

public class AssignElementExecutor extends BaseElementExecutor<AssignElement> {
    public AssignElementExecutor(AssignElement element) {
        super(element);
    }

    @Override
    public void visit(Context context) {
        context.setResponse(element.getName().getName(), element.getValue());
    }
}
