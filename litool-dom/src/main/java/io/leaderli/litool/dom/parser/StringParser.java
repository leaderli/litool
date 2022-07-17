package io.leaderli.litool.dom.parser;

import io.leaderli.litool.dom.LiDomParser;

/**
 * @author leaderli
 * @since 2022/7/7 8:23 AM
 */
public class StringParser implements LiDomParser<String> {

    @Override
    public String apply(String s) {
        return s;
    }

    @Override
    public Class<String> componentType() {
        return null;
    }
}
