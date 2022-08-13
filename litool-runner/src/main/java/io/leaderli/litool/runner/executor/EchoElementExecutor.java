package io.leaderli.litool.runner.executor;

import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.xml.router.task.EchoElement;

public class EchoElementExecutor extends BaseElementExecutor<EchoElement> {
    public EchoElementExecutor(EchoElement element) {
        super(element);
    }

    @Override
    public void visit(Context context) {
        // TODO ECHO简单实现
        System.out.println(element.getMsg());
    }
}
