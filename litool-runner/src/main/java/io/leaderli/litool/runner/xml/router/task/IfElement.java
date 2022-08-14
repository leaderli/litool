package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.constant.VariablesModel;
import io.leaderli.litool.runner.executor.router.task.IfElementExecutor;

import static io.leaderli.litool.runner.constant.VariablesModel.*;

public class IfElement extends TaskElement<IfElement, IfElementExecutor> {

    private Expression cond = new Expression("true");

    public void addAssign(AssignElement element) {
        taskList.add(element);
    }

    public void addGoto(GotoElement element) {
        taskList.add(element);
    }

    public void addEcho(EchoElement element) {
        taskList.add(element);
    }

    @Override
    public String tag() {
        return "if";
    }

    public Expression getCond() {
        return cond;
    }

    public void setCond(Expression cond) {
        String name = cond.getName();
        LiAssertUtil.assertTrue(StringUtils.isBlank(name)
                        || cond.getModel().matchAny(FUNC, REQUEST, LITERAL)
                , "variable cond can only use func ref or request ref or string");

        if (cond.getModel() == VariablesModel.LITERAL) {
            cond.setObject(StringUtils.equals(name, "") || Boolean.parseBoolean(name));
        }
        this.cond = cond;
    }

}
