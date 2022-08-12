package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.LiConstant;
import io.leaderli.litool.dom.sax.AttributeEvent;
import io.leaderli.litool.dom.sax.BodyEvent;
import io.leaderli.litool.runner.executor.AssignElementExecutor;

public class AssignElement extends TaskElement<AssignElement, AssignElementExecutor> {

    private String name;
    private String value;

    @Override
    public void attribute(AttributeEvent attributeEvent) {

        super.attribute(attributeEvent);
    }

    @Override
    public void body(BodyEvent bodyEvent) {
        String value = bodyEvent.description();
        LiAssertUtil.assertTrue(value.matches(LiConstant.ATTRIBUTE_NAME_RULE), String.format("the entry key %s is not match %s", value, LiConstant.ATTRIBUTE_NAME_RULE));
        this.value = value;
    }

    @Override
    public String name() {
        return "assign";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public AssignElementExecutor executor() {
        return new AssignElementExecutor(this);
    }
}
