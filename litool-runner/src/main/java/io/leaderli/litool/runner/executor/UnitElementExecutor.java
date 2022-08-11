package io.leaderli.litool.runner.executor;

import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.xml.router.UnitElement;

public class UnitElementExecutor extends BaseElementExecutor<UnitElement> {

    public UnitElementExecutor(UnitElement element) {
        super(element);
    }

    @Override
    public void visit(Context context) {
        // TODO 依次判断if是否满足条件
    }
}
