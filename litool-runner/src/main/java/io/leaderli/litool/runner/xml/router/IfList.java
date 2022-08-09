package io.leaderli.litool.runner.xml.router;

import io.leaderli.litool.dom.sax.SaxList;

public class IfList extends SaxList<IfElement> {
    @Override
    public Class<IfElement> componentType() {
        return IfElement.class;
    }
}
