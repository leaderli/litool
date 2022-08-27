package io.leaderli.litool.runner.xml.router;

import io.leaderli.litool.dom.sax.SaxList;

public class UnitList extends SaxList<UnitElement> {
@Override
public Class<UnitElement> componentType() {
    return UnitElement.class;
}
}
