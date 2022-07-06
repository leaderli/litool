package io.leaderli.litool.dom.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/7/7
 */
public class LiDomDFSContext {

    public final LiDomDFSContext parent;

    public final Map<String, Object> objectMap = new HashMap<>();

    public LiDomDFSContext(LiDomDFSContext parent) {
        this.parent = parent;
    }
}
