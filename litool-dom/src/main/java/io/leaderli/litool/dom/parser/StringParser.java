package io.leaderli.litool.dom.parser;

import io.leaderli.litool.dom.LiDomParser;
import org.dom4j.Attribute;

/**
 * @author leaderli
 * @since 2022/7/7 8:23 AM
 */
public class StringParser implements LiDomParser<String, Attribute> {


    @Override
    public Class<Attribute> componentType() {
        return Attribute.class;
    }

    @Override
    public String parse(Attribute node) {
        return node.getStringValue();
    }
}
