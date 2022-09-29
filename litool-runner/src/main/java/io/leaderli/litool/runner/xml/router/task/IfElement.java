package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.constant.VariablesModel;
import io.leaderli.litool.runner.executor.router.task.IfElementExecutor;

public class IfElement extends TaskElement<IfElement, IfElementExecutor> {

    private Expression cond = new Expression(true, VariablesModel.LITERAL);

    public IfElement() {
        super("if");
    }

    public void addAssign(AssignElement element) {
        taskList.add(element);
    }

    public void addGoto(GotoElement element) {
        taskList.add(element);
    }

    public void addEcho(EchoElement element) {
        taskList.add(element);
    }

    public void addCoordinate(CoordinateElement element) {
        taskList.add(element);
    }

    public void addKafka(KafkaElement element) {
        taskList.add(element);
    }


    public Expression getCond() {
        return cond;
    }

    public void setCond(Expression cond) {
        String name = cond.getName();
        LiAssertUtil.assertTrue(StringUtils.isBlank(name)
                        || cond.getModel().matchAny(VariablesModel.FUNC, VariablesModel.REQUEST, VariablesModel.LITERAL)
                , "variable cond can only use func ref or request ref or string");


        if (cond.getModel() == VariablesModel.LITERAL) {
            cond.setObject(StringUtils.equals(name, "") || Boolean.parseBoolean(name));
        }
        this.cond = cond;
    }

}
