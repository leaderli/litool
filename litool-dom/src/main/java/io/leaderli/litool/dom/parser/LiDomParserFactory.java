package io.leaderli.litool.dom.parser;

import io.leaderli.litool.dom.LiDomParser;
import org.dom4j.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/7/6 8:52 AM
 */
public class LiDomParserFactory {


    private static final Map<Class<?>, LiDomParser> parserMap = new HashMap<>();


    public static <T, E extends Node> LiDomParser<T, E> get(Class<T> targetType, Class<E> nodeType) {
        return parserMap.get(targetType);
    }

}
