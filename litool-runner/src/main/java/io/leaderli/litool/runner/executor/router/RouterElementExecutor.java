package io.leaderli.litool.runner.executor.router;

import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.ContextVisitor;
import io.leaderli.litool.runner.Interrupt;
import io.leaderli.litool.runner.executor.BaseElementExecutor;
import io.leaderli.litool.runner.xml.router.RouterElement;
import io.leaderli.litool.runner.xml.router.SequenceElement;

import java.util.HashMap;
import java.util.Map;

public class RouterElementExecutor extends BaseElementExecutor<RouterElement> {

// 其他流程缓存到context中供后续goto调用
private final Map<String, ContextVisitor> sequenceExecutorMap = new HashMap<>();
// 保存主流程，routerExecutor直接调用主流程
private String defaultSequenceName;

public RouterElementExecutor(RouterElement element) {
    super(element);
    init();
}

private void init() {
    defaultSequenceName = element.getSequenceList().lira().first().get().getName();
    for (SequenceElement sequenceElement : element.getSequenceList().lira()) {
        sequenceExecutorMap.put(sequenceElement.getName(), sequenceElement.executor());
    }
}

@Override
public void execute(Context context) {
    // 第一个sequence为主流程
    // 取临时字段，为空时调用主流程

    sequenceExecutorMap.get(defaultSequenceName).visit(context);
}

@Override
public boolean notify(Context context) {
    if (context.interrupt.have(Interrupt.GOTO)) {
        context.interrupt.disable(Interrupt.GOTO);
        String next = (String) context.interruptObj;
        sequenceExecutorMap.get(next).visit(context);
    }
    return false;
}
}
