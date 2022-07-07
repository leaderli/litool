package io.leaderli.litool.dom;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.type.ComponentType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leaderli
 * @since 2022/7/9 10:32 AM
 */
public abstract class SaxBean<T extends SaxBean<?>> implements ComponentType<T> {

    public String body = "";

    public List<T> children = new ArrayList<>();


    public void add(Object t) {
        Lino.of(t).cast(componentType()).ifPresent(children::add);
    }

    @Override
    public String toString() {
        return "SaxBean{" +
                "body='" + body + '\'' +
                ", children=" + children +
                '}';
    }
}
