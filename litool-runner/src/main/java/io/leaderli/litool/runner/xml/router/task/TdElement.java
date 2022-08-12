package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.dom.sax.BodyEvent;
import io.leaderli.litool.dom.sax.SaxBean;

public class TdElement implements SaxBean {

    private String value;


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void body(BodyEvent bodyEvent) {
        String value = bodyEvent.description();
        LiAssertUtil.assertTrue(value.matches(TD_VALUE_RULE),String.format("the td value %s is not match %s",value, TD_VALUE_RULE));
        this.value = value;
    }

    @Override
    public String name() {
        return "td";
    }

    public static final String TD_VALUE_RULE = "(?!,).+(?<!,)";

}
