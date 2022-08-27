package io.leaderli.litool.runner.executor.router.task;

import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.executor.BaseElementExecutor;
import io.leaderli.litool.runner.xml.router.task.AssignElement;

public class AssignElementExecutor extends BaseElementExecutor<AssignElement> {
public AssignElementExecutor(AssignElement element) {
    super(element);
}

@Override
public void execute(Context context) {
    context.setResponse(element.getName().getName(), context.getExpressionValue(element.getValue()));
}
}
