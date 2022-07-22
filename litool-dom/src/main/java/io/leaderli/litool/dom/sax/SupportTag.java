package io.leaderli.litool.dom.sax;

import io.leaderli.litool.core.lang.TupleMap;

/**
 * @author leaderli
 * @since 2022/7/14
 */
public interface SupportTag {


    default TupleMap<String, Class<SaxBean>> support() {
        return TupleMap.of();
    }

    default String tagName() {
        return this.getClass().getSimpleName().toLowerCase();
    }

}
