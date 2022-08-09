package io.leaderli.litool.runner.xml.router;

import io.leaderli.litool.dom.sax.SaxList;

public class IfTaskList extends SaxList<IfTaskElement> {
    @Override
    public Class<IfTaskElement> componentType() {
        return IfTaskElement.class;
    }

}
