package io.leaderli.litool.dom;

import io.leaderli.litool.core.lang.TupleMap;
import io.leaderli.litool.dom.sax.SaxList;
import io.leaderli.litool.dom.sax.SupportTagBuilder;

/**
 * @author leaderli
 * @since 2022/7/23
 */
class BeanSaxList extends SaxList<Bean> {




    @Override
    public Class<Bean> componentType() {
        return Bean.class;
    }
}
