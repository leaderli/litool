package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.dom.sax.BodyEvent;
import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.constant.VariablesModel;
import io.leaderli.litool.runner.executor.router.task.AssignElementExecutor;

public class AssignElement extends BaseElement<AssignElement, AssignElementExecutor> {

    private Expression name;
    private String value;

    public AssignElement() {
        super("assign");
    }

    @Override
    public void body(BodyEvent bodyEvent) {
        this.value = bodyEvent.description();
    }


    public Expression getName() {
        return name;
    }

    public void setName(Expression name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    @Override
    public Lino<?> complexField(Class<?> parameterType, String value) {
        if (parameterType == Expression.class) {
            Expression expression = new Expression();
            expression.setModel(VariablesModel.RESPONSE);
            expression.setName(value);
            return Lino.of(expression);
        }
        return super.complexField(parameterType, value);
    }
}
