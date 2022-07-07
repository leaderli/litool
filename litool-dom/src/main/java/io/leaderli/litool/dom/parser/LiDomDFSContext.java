package io.leaderli.litool.dom.parser;

import io.leaderli.litool.core.meta.LiBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/7/7
 */
public class LiDomDFSContext {

    public final LiDomDFSContext parent;

    public final LiBox<Class<?>> tag = LiBox.none();
    public final Map<String, String> attributes = new HashMap<>();
    public final List<Object> children = new ArrayList<>();

    public LiDomDFSContext(LiDomDFSContext parent) {
        this.parent = parent;
    }

}
