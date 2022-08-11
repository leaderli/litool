package io.leaderli.litool.runner.xml.funcs;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.dom.sax.EndEvent;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.constant.VariablesModel;
import io.leaderli.litool.runner.executor.ElementExecutor;
import io.leaderli.litool.runner.executor.FuncsElementExecutor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FuncsElement implements SaxBean, ElementExecutor<FuncsElementExecutor> {

    private FuncList funcList = new FuncList();

    public void addFunc(FuncElement funcElement) {
        funcList.add(funcElement);
    }

    @Override
    public void end(EndEvent endEvent) {
        LiAssertUtil.assertFalse(funcList.lira().size() == 0, "the funcList of funcs is empty");

        Set<String> nameSet = new HashSet<>();
        Set<String> labelSet = new HashSet<>();
        for (FuncElement funcElement : funcList.lira()) {
            List<String> map = funcElement.getParamList().lira()
                    .map(ParamElement::getExpression)
                    .filter(expr -> expr.getModel() == VariablesModel.FUNC)
                    .map(Expression::getName)
                    .filter(paramName -> !nameSet.contains(paramName))
                    .map(paramName -> String.format("error reference [%s]:[%s] can only use func ref defined before this.", paramName, funcElement.getName())).getRaw();
            LiAssertUtil.assertTrue(map.isEmpty(), () -> StringUtils.join("\n", map.toArray()));

            LiAssertUtil.assertTrue(nameSet.add(funcElement.getName()), "duplicate name of " + funcElement.getName());
            LiAssertUtil.assertTrue(labelSet.add(funcElement.getLabel()), "duplicate label of " + funcElement.getLabel());

        }

        SaxBean.super.end(endEvent);
    }

    @Override
    public String name() {
        return "funcs";
    }

    public FuncList getFuncList() {
        return funcList;
    }

    public void setFuncList(FuncList funcList) {
        this.funcList = funcList;
    }

    @Override
    public FuncsElementExecutor executor() {
        return new FuncsElementExecutor(this);
    }
}