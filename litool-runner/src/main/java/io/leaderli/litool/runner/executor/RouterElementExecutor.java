package io.leaderli.litool.runner.executor;

import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.TempNameEnum;
import io.leaderli.litool.runner.constant.UnitStateConstant;
import io.leaderli.litool.runner.xml.router.RouterElement;
import io.leaderli.litool.runner.xml.router.SequenceElement;
import io.leaderli.litool.runner.xml.router.task.EchoElement;

import java.util.HashMap;
import java.util.Map;

public class RouterElementExecutor extends BaseElementExecutor<RouterElement> {

    // 保存主流程，routerExecutor直接调用主流程
    private String defaultSequenceName;
    // 其他流程缓存到context中供后续goto调用
    private Map<String, SequenceElementExecutor> sequenceExecutorMap = new HashMap<>();

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
    public void visit(Context context) {
        // 第一个sequence为主流程
        // 取临时字段，为空时调用主流程
        String tempName = defaultSequenceName;
        while (StringUtils.isNotBlank(tempName)) {
            SequenceElementExecutor sequenceElementExecutor = sequenceExecutorMap.get(tempName);
            context.setTemp(TempNameEnum.sequence_name.name(), "");
            if (sequenceElementExecutor != null) {
                context.setTemp(TempNameEnum.unit_state.name(), UnitStateConstant.CONTINUE);
                sequenceElementExecutor.visit(context);
            }
            tempName = context.getTemp(TempNameEnum.sequence_name.name());
        }
    }

}
