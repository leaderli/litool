package io.leaderli.litool.dom;

import io.leaderli.litool.core.lang.LiTupleMap;

/**
 * @author leaderli
 * @since 2022/7/14
 */
public interface SupportTag {


    default LiTupleMap<String, Class<SaxBean>> support() {
        return LiTupleMap.of();
    }


    default String tagName() {
        return this.getClass().getSimpleName().toLowerCase();
    }

}
