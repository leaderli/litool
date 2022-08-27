package io.leaderli.litool.runner.xml.funcs;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.dom.sax.BodyEvent;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.TypeAlias;
import io.leaderli.litool.runner.constant.VariablesModel;

import java.util.List;

public class ParamElement extends SaxBean {

private String type = "str";
private Expression expression = new Expression("", VariablesModel.LITERAL);

public ParamElement() {
    super("param");
}

@Override
public void body(BodyEvent bodyEvent) {
    String expr = bodyEvent.description();

    this.expression = new Expression(expr);
}

@Override
public void end_check(List<String> parseErrorMsgs) {

    if (this.expression.getModel() == VariablesModel.LITERAL) {

        TypeAlias.check(this.type, this.expression.getName(), String.format("%s cannot parse to %s",
                this.expression.getName(), this.type));
    }
    super.end_check(parseErrorMsgs);
}

public String getType() {
    return type;
}

public void setType(String type) {
    if (StringUtils.isEmpty(type)) {
        return;
    }
    LiAssertUtil.assertTrue(TypeAlias.support(type), String.format("the param type %s is unsupported ", type));
    this.type = type;
}

public Expression getExpression() {
    return expression;
}

public void setExpression(Expression expression) {
    this.expression = expression;
}

}
