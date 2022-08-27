package io.leaderli.litool.dom.sax;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ComponentType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leaderli
 * @since 2022/7/9 10:32 AM
 * <p>
 * 用于多个不同标签名的集合类标签
 */
public abstract class SaxList<S extends SaxBean> implements ComponentType<S> {

private final List<S> children = new ArrayList<>();


public void add(S t) {
    children.add(t);
}

public Lira<S> lira() {
    return Lira.of(children);
}

}
