package io.leaderli.litool.dom;

import io.leaderli.litool.core.lang.TupleMap;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.dom.sax.SaxList;
import io.leaderli.litool.dom.sax.SupportTagBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leaderli
 * @since 2022/7/23
 */
class BeanSaxList implements SaxList<SaxBean> {

    public final List<SaxBean> children = new ArrayList<>();

    @Override
    public void add(SaxBean t) {

        this.children.add(t);
    }

    @Override
    public TupleMap<String, Class<SaxBean>> support() {
        return SupportTagBuilder.of(SaxList.super.support())
                .add(Bean.class)
                .build();
    }

}
