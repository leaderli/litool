package io.leaderli.litool.runner;

import io.leaderli.litool.core.lang.TupleMap;
import io.leaderli.litool.dom.sax.SaxList;
import io.leaderli.litool.dom.sax.SupportTagBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/7/24
 */
public class EntryList implements SaxList<EntryElement> {


    private final Map<String, EntryElement> children = new HashMap<>();

    @Override
    public void add(EntryElement entryElement) {
        children.put(entryElement.key.text, entryElement);
    }

    @Override
    public TupleMap<String, Class<EntryElement>> support() {
        return SupportTagBuilder.of(EntryElement.class).build();
    }
}
