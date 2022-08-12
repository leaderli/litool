package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.dom.sax.SaxList;

@SuppressWarnings("rawtypes")
public class TaskList extends SaxList<TaskElement> {
    @Override
    public Class<TaskElement> componentType() {
        return TaskElement.class;
    }
}
