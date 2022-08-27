package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.dom.sax.BodyEvent;
import io.leaderli.litool.dom.sax.SaxBean;

import java.util.List;

public class TdElement extends SaxBean {

public static final String TD_VALUE_RULE = "(?!,).+(?<!,)";
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
    String description = bodyEvent.description();
    LiAssertUtil.assertTrue(description.matches(TD_VALUE_RULE), String.format("the td value %s is not match %s",
            description, TD_VALUE_RULE));
    this.value = Lira.of(StringUtils.split(description, ",")).get();
}

}
