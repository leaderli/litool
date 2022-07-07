package io.leaderli.litool.dom;

import io.leaderli.litool.core.exception.RuntimeExceptionTransfer;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.util.LiPrintUtil;
import io.leaderli.litool.core.util.LiStrUtil;
import org.junit.jupiter.api.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
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


            @SuppressWarnings("rawtypes")
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {


                SaxBean<?> peek = null;

                if (!stack.isEmpty()) {
                    peek = stack.peek();
                }

                Class<? extends SaxBean<?>> tagBeanClass = LiDomTagBean.getTagBeanClass(null, qName);
                SaxBean<?> o = RuntimeExceptionTransfer.get(tagBeanClass::newInstance);

                Lino.of(peek).ifPresent(p -> p.add(o));

                Lira.of(tagBeanClass.getFields()).filter(f -> !f.getType().isArray()).forEach(field -> {

                    String name = field.getName();
                    String value = attributes.getValue(name);

                    LiPrintUtil.print(name, value + "");
                    if (value != null) {

                        RuntimeExceptionTransfer.run(() -> {
                            field.set(o, value);
                        });
                    }
                });

                stack.push(o);
                LiPrintUtil.print("--->", stack);

            }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                String trim = new String(ch, start, length).trim();
                if (LiStrUtil.isEmpty(trim)) {
                    return;
                }
                LiPrintUtil.print("----", stack, trim);

            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
                LiPrintUtil.print("<---", stack);

                Object peek = stack.peek();


                stack.pop();
            }
        });
    }
}
