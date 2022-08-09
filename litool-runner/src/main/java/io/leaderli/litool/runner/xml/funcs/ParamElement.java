package io.leaderli.litool.runner.xml.funcs;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.dom.sax.BodyEvent;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.runner.TypeAlias;
import io.leaderli.litool.runner.util.ExpressionUtil;

import java.util.regex.Pattern;

public class ParamElement implements SaxBean {

    private String type;
    private String value;

    @Override
    public void body(BodyEvent bodyEvent) {
        String value = bodyEvent.description();
        LiAssertUtil.assertTrue(ExpressionUtil.expressionCheck(value), "param value is illegal");

        this.value = value;
    }

    @Override
    public String name() {
        return "param";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        LiAssertUtil.assertTrue(TypeAlias.support(type), String.format("the param type %s is unsupported ", type));
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
