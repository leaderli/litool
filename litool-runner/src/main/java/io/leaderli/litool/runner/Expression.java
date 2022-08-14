package io.leaderli.litool.runner;

import io.leaderli.litool.runner.constant.VariablesModel;
import io.leaderli.litool.runner.util.ExpressionUtil;

public class Expression {

    private Object name;
    private VariablesModel model;

    public Expression() {
    }

    public Expression(String expr) {
        Expression expression = ExpressionUtil.getExpression(expr);
        this.name = expression.getName();
        this.model = expression.getModel();
    }

    public Expression(Object name, VariablesModel model) {
        this.name = name;
        this.model = model;
    }

    public void setObject(Object obj) {
        this.name = obj;
    }

    public Object getObject() {
        return name;
    }

    public String getName() {
        return name + "";
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

    public Object apply(Context context) {
        return model.apply(context, name);
    }

    @Override
    public String toString() {
        return this.model + ":" + this.name;
    }
}
