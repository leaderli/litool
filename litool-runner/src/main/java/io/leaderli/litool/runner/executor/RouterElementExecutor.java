package io.leaderli.litool.runner.executor;

import io.leaderli.litool.core.collection.ImmutableMap;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.xml.router.RouterElement;
import io.leaderli.litool.runner.xml.router.SequenceElement;

import java.util.HashMap;
import java.util.Map;

public class RouterElementExecutor extends BaseElementExecutor<RouterElement> {

    // 保存主流程，routerExecutor直接调用主流程
    private SequenceElementExecutor mainSequenceExecutor;
    // 其他流程缓存到context中供后续goto调用
    private Map<String, SequenceElementExecutor> sequenceExecutorMap = new HashMap<>();

    public RouterElementExecutor(RouterElement element) {
        super(element);
        init();
    }

    private void init() {
        for (SequenceElement sequenceElement : element.getSequenceList().lira()) {
            if (mainSequenceExecutor == null) {
                mainSequenceExecutor = sequenceElement.executor();
            } else {
                sequenceExecutorMap.put(sequenceElement.getName(), sequenceElement.executor());
            }
        }
    }

    @Override
    public void visit(Context context) {
//        context.setSequenceExecutorMap(ImmutableMap.of(sequenceExecutorMap));

        mainSequenceExecutor.visit(context);
    }

}
