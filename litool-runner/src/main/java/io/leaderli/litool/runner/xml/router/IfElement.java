package io.leaderli.litool.runner.xml.router;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.constant.VariablesModel;

public class IfElement implements SaxBean {

    private Expression cond;

    private IfTaskList ifTaskList = new IfTaskList();

    public void addAssign(IfTaskElement element) {
        ifTaskList.add(element);
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
}
