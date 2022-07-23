package io.leaderli.litool.dom.sax;

import io.leaderli.litool.core.lang.TupleMap;

/**
 * @author leaderli
 * @since 2022/7/9 10:32 AM
 * <p>
 * 用于多个不同标签名的集合类标签
 */
public interface SaxList<T extends SaxBean> {


    void add(T t);

    /**
     * @return 返回当前类支持的 SaxList 支持的子元素
     */
    default TupleMap<String, Class<T>> support() {
        return TupleMap.of();
    }


}
