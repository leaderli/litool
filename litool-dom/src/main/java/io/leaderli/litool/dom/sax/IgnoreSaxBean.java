package io.leaderli.litool.dom.sax;

/**
 * @author leaderli
 * @since 2022/7/24
 */
public class IgnoreSaxBean extends SaxBean implements SaxEventHandler {


    public IgnoreSaxBean() {
        super("");
    }

    @Override
    public void start(StartEvent startEvent) {

    }

    @Override
    public void attribute(AttributeEvent attributeEvent) {
    }

    @Override
    public void body(BodyEvent bodyEvent) {

    }

    @Override
    public void end(EndEvent endEvent) {

    }


}
