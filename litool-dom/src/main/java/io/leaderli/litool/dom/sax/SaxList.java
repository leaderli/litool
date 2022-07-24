package io.leaderli.litool.dom.sax;

import io.leaderli.litool.core.lang.TupleMap;
import io.leaderli.litool.core.type.ComponentType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leaderli
 * @since 2022/7/9 10:32 AM
 * <p>
 * 用于多个不同标签名的集合类标签
 */
public abstract class SaxList<T extends SaxBean> implements ComponentType<T> {

    private final List<T> children = new ArrayList<>();


    public void add(T entryElement) {
        children.add(entryElement);
    }

    /**
     * @return 返回当前类支持的 SaxList 支持的子元素
     */
    public TupleMap<String, Class<T>> support() {
        return TupleMap.of();
    }

    public List<T> copy() {
        return new ArrayList<>(children);
    }

}
