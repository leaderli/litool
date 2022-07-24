package io.leaderli.litool.dom;

import io.leaderli.litool.core.lang.TupleMap;
import io.leaderli.litool.dom.sax.SaxList;
import io.leaderli.litool.dom.sax.SupportTagBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leaderli
 * @since 2022/7/23
 */
class BeanSaxList extends SaxList<Bean> {

    public final List<Bean> children = new ArrayList<>();

    @Override
    public void add(Bean t) {

        this.children.add(t);
    }

    @Override
    public TupleMap<String, Class<Bean>> support() {
        return SupportTagBuilder.of(super.support())
                .add(Bean.class)
                .build();
    }

    @Override
    public List<Bean> copy() {
        return new ArrayList<>(children);
    }


    @Override
    public Class<Bean> componentType() {
        return Bean.class;
    }
}
