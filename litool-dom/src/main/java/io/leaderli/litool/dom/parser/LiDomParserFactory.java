package io.leaderli.litool.dom.parser;

import io.leaderli.litool.dom.LiDomParser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/7/6 8:52 AM
 */
public class LiDomParserFactory {


    private static final Map<Class<?>, LiDomParser> parserMap = new HashMap<>();


    public static <T> LiDomParser<T> get(Class<T> type) {
        return parserMap.get(type);
    }

}
