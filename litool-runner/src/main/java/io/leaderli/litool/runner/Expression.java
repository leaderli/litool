package io.leaderli.litool.runner;

import io.leaderli.litool.core.meta.LiTuple2;
import io.leaderli.litool.runner.constant.VariablesModel;
import io.leaderli.litool.runner.util.ExpressionUtil;

public class Expression {

    private String name;
    private VariablesModel model;

    public Expression() {
    }

    public Expression(String expr) {
        LiTuple2<String, VariablesModel> stringVariablesModelLiTuple2 = ExpressionUtil.getExpression(expr);
        this.name = stringVariablesModelLiTuple2._1;
        this.model = stringVariablesModelLiTuple2._2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VariablesModel getModel() {
        return model;
    }

    public void setModel(VariablesModel model) {
        this.model = model;
    }
}
