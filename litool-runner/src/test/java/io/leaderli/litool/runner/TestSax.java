package io.leaderli.litool.runner;

import io.leaderli.litool.core.util.ConsoleUtil;
import org.junit.jupiter.api.Test;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @author leaderli
 * @since 2022/7/9 9:00 AM
 */
public class TestSax {

    @Test
    public void test() throws Throwable {

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();

        saxParser.parse(TestSax.class.getResourceAsStream("/request.xml"), new DefaultHandler() {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) {
                ConsoleUtil.print(qName, attributes);
            }

            @Override
            public void characters(char[] ch, int start, int length) {
                ConsoleUtil.print("body", new String(ch, start, length));
            }
        });
    }
}
