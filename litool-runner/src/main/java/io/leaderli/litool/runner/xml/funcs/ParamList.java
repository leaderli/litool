package io.leaderli.litool.runner.xml.funcs;

import io.leaderli.litool.dom.sax.SaxList;

public class ParamList extends SaxList<ParamElement> {
    @Override
    public Class<ParamElement> componentType() {
        return ParamElement.class;
    }
}
