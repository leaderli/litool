package io.leaderli.litool.runner.xml.funcs;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.dom.sax.BodyEvent;
import io.leaderli.litool.dom.sax.EndEvent;
import io.leaderli.litool.runner.Expression;
import io.leaderli.litool.runner.TypeAlias;
import io.leaderli.litool.runner.xml.SaxBeanWithID;

public class ParamElement extends SaxBeanWithID {

    private String type = "str";
    private Expression expression;

    @Override
    public void body(BodyEvent bodyEvent) {
        String expr = bodyEvent.description();

        this.expression = new Expression(expr);
    }

    @Override
    public void end(EndEvent endEvent) {
        super.end(endEvent);
    }

    @Override
    public String name() {
        return "param";
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
