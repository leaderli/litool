package io.leaderli.litool.dom;

import io.leaderli.litool.core.lang.TupleMap;
import io.leaderli.litool.dom.sax.SaxBean;
import io.leaderli.litool.dom.sax.SaxList;
import io.leaderli.litool.dom.sax.SupportTagBuilder;

/**
 * @author leaderli
 * @since 2022/7/23
 */
class BeanSaxList extends SaxList {
    @Override
    public TupleMap<String, Class<SaxBean>> support() {
        return SupportTagBuilder.of(super.support())
                .add(Bean.class)
                .build();
    }

}
