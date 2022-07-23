package io.leaderli.litool.dom;

import io.leaderli.litool.dom.sax.SaxBody;

import java.util.Map;

/**
 * @author leaderli
 * @since 2022/7/24
 */
public class MapSaxBody extends SaxBody {
    public MapSaxBody(String text) {
        super(text);
    }

    public String get(Map<String, String> map) {
        return map.get(text);
    }
}
