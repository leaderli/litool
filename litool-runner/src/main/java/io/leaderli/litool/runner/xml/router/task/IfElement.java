package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.constant.VariablesModel;
import io.leaderli.litool.runner.executor.IfElementExecutor;

public class IfElement extends TaskElement<IfElement, IfElementExecutor> {

    private Expression cond;

    public void addAssign(AssignElement element) {
        taskList.add(element);
    }

    @Override
    public String name() {
        return "if";
    }

    public Expression getCond() {
        return cond;
    }

    public void setCond(Expression cond) {
        LiAssertUtil.assertTrue(cond.getModel() == VariablesModel.FUNC
                        || cond.getModel() == VariablesModel.REQUEST
                , "variable cond can only use func ref or request ref");

        this.cond = cond;
    }

    @Override
    public IfElementExecutor executor() {
        return new IfElementExecutor(this);
    }
}
