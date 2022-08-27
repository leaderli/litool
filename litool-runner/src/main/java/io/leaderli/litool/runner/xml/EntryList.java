package io.leaderli.litool.runner.xml;

import io.leaderli.litool.dom.sax.SaxList;

/**
 * @author leaderli
 * @since 2022/7/24
 */
public class EntryList extends SaxList<EntryElement> {
@Override
public Class<EntryElement> componentType() {
    return EntryElement.class;
}
}
