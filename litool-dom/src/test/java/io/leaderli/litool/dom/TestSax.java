package io.leaderli.litool.dom;

import io.leaderli.litool.core.exception.RuntimeExceptionTransfer;
import io.leaderli.litool.core.lang3.LiStringUtils;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.LiReflectUtil;
import io.leaderli.litool.core.util.LiPrintUtil;
import org.junit.jupiter.api.Test;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.Stack;

/**
 * @author leaderli
 * @since 2022/7/9 9:00 AM
 */
public class TestSax {

    @Test
    public void test() throws Throwable {


        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();

        Stack<SaxBean<?>> stack = new Stack<>();

        saxParser.parse(TestSax.class.getResourceAsStream("/bean.xml"), new DefaultHandler() {


            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) {


                SaxBean<?> peek = null;

                if (!stack.isEmpty()) {
                    peek = stack.peek();
                }

                Class<? extends SaxBean<?>> tagBeanClass = LiDomTagBean.getTagBeanClass(null, qName);
                SaxBean<?> saxBean = RuntimeExceptionTransfer.get(tagBeanClass::newInstance);

                Lino.of(peek).ifPresent(p -> p.add(saxBean));

                for (int i = 0; i < attributes.getLength(); i++) {
                    String name = attributes.getLocalName(i);
                    String value = attributes.getValue(i);

                    LiReflectUtil.setFieldValue(saxBean, name, value, true);
                }

                Lira.of(tagBeanClass.getFields())
                        .filter(f -> LiReflectUtil.getFieldValue(saxBean, f).absent())
                        .forEach(f -> LiPrintUtil.print("########", f));


                stack.push(saxBean);
                LiPrintUtil.print("--->", stack);

            }

            @Override
            public void characters(char[] ch, int start, int length) {
                String trim = new String(ch, start, length).trim();
                if (LiStringUtils.isEmpty(trim)) {
                    return;
                }
                LiPrintUtil.print("----", stack, trim);

            }

            @Override
            public void endElement(String uri, String localName, String qName) {

                Object pop = stack.pop();

                if (stack.isEmpty()) {
                    LiPrintUtil.print("~~~~~~~~~", pop);
                }

            }
        });
    }
}
