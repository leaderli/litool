package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.dom.sax.SaxList;


public class TdList extends SaxList<TdElement> {
@Override
public Class<TdElement> componentType() {
    return TdElement.class;
}
}
