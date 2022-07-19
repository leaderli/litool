package io.leaderli.litool.dom.parser;

import io.leaderli.litool.core.lang.TupleMap;
import io.leaderli.litool.dom.LiDomParser;

/**
 * @author leaderli
 * @since 2022/7/6 8:52 AM
 */
public class LiDomParserFactory {


    private static TupleMap<Class<?>, LiDomParser<?>> TYPE_PARSER = new TupleMap<>();

    static {

        StringParser stringParser = new StringParser();
        TYPE_PARSER.putKeyValue(stringParser.componentType(), stringParser);

    }

//
//    public static <T> Lino<T> create(Class<T> type, String value) {
//
//
//        return Lino.of(TYPE_PARSER.get(type))
//    }

}
