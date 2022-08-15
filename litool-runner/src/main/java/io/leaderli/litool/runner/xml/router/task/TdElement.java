package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.dom.sax.BodyEvent;
import io.leaderli.litool.dom.sax.SaxBean;

import java.util.List;

public class TdElement extends SaxBean {

    private List<String> value;

    public TdElement() {
        super("td");
    }


    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    @Override
    public void body(BodyEvent bodyEvent) {
        String value = bodyEvent.description();
        LiAssertUtil.assertTrue(value.matches(TD_VALUE_RULE),String.format("the td value %s is not match %s",value, TD_VALUE_RULE));
        this.value = Lira.of(StringUtils.split(value,",")).getRaw();
    }

    @Override
    public String tag() {
        return "td";
    }

    public static final String TD_VALUE_RULE = "(?!,).+(?<!,)";

}
