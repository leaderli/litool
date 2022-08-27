package io.leaderli.litool.runner.xml.funcs;

import io.leaderli.litool.dom.sax.SaxList;

public class FuncList extends SaxList<FuncElement> {

@Override
public Class<FuncElement> componentType() {
    return FuncElement.class;
}

}
