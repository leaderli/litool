package io.leaderli.litool.dom.sax;

import io.leaderli.litool.core.lang.TupleMap;

/**
 * @author leaderli
 * @since 2022/7/14
 */
public interface SupportTag {


    /**
     * @return 返回当前类支持的 SaxList 支持的子元素
     */
    default TupleMap<String, Class<SaxBean>> support() {
        return TupleMap.of();
    }


}
