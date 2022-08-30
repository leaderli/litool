package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.dom.sax.SaxList;

@SuppressWarnings("rawtypes")
public class TaskList extends SaxList<BaseElement> {
    @Override
    public Class<BaseElement> componentType() {
        return BaseElement.class;
    }
}
