package io.leaderli.litool.dom.parser;

import io.leaderli.litool.core.exception.RuntimeExceptionTransfer;
import io.leaderli.litool.core.type.ComponentType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/7/7
 */
public class LiDomDFSContext<Child, Parent> implements ComponentType<Child> {

    public final LiDomDFSContext<Parent, ?> parent;

    public Class<?> type;
    public String content;
    public Child bean;


    public final Map<String, String> attributes = new HashMap<>();
    public final List<Object> children = new ArrayList<>();

    public LiDomDFSContext(LiDomDFSContext<Parent, ?> parent) {
        this.parent = parent;
    }

    public void newInstance() {
        Child instance = RuntimeExceptionTransfer.get(() -> componentType().getConstructor(LiDomDFSContext.class).newInstance(this));

        this.bean = instance;

        if (parent != null) {
            parent.children.add(instance);
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<Child> componentType() {
        return (Class<Child>) type;
    }
}
