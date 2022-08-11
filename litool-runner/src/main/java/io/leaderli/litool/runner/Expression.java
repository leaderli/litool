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
        LiTuple2<String, VariablesModel> name_model = ExpressionUtil.getExpression(expr);
        this.name = name_model._1;
        this.model = name_model._2;
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

    public Object apply(Context context) {
        return model.apply(context, name);
    }

    @Override
    public String toString() {
        return  this.model+":"+this.name;
    }
}
