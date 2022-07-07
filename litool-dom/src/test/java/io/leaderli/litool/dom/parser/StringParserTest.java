package io.leaderli.litool.dom.parser;

import org.dom4j.Attribute;
import org.dom4j.QName;
import org.dom4j.dom.DOMAttribute;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/7/7 8:23 AM
 */
class StringParserTest {

    @Test
    void parse() {
        Attribute attribute = new DOMAttribute(QName.get("name"), "attr");

        Assertions.assertEquals("attr", new StringParser().parse(attribute));
        attribute = new DOMAttribute(QName.get("name"), null);
        Assertions.assertNull(new StringParser().parse(attribute));
    }
}
