package io.leaderli.litool.runner.xml.router;

import io.leaderli.litool.dom.sax.SaxList;

public class SequenceList extends SaxList<SequenceElement> {
@Override
public Class<SequenceElement> componentType() {
    return SequenceElement.class;
}
}
