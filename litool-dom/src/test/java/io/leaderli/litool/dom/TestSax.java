package io.leaderli.litool.dom;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.lang3.LiStringUtils;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.LiReflectUtil;
import io.leaderli.litool.core.util.LiPrintUtil;
import io.leaderli.litool.dom.sax.RootBean;
import org.junit.jupiter.api.Test;
import org.xml.sax.Attributes;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.lang.reflect.Field;
import java.util.List;
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

        Stack<SaxBean> stack = new Stack<>();

        saxParser.parse(TestSax.class.getResourceAsStream("/bean.xml"), new LocatorDefaultHandler() {


            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) {

                if (stack.isEmpty()) {
                    populateProperties(null, new RootBean(), attributes);
                    return;
                }

                SaxBean peek = stack.peek();

                Lino.of(peek.support())
                        .map(pair -> pair.getValueByKey(qName.toLowerCase()).get())
                        .throwable_map(Class::newInstance)
                        .assertNotNone(String.format("not support to handle <%s> in <%s> at line %d , column %d", qName, peek.tagName(), locator.getLineNumber(), locator.getColumnNumber()))
                        .ifPresent(saxBean -> populateProperties(peek, saxBean, attributes));


            }

            private void populateProperties(SaxBean parent, SaxBean saxBean, Attributes attributes) {
                //注册到父节点中
                Lino.of(parent).ifPresent(p -> p.add(saxBean));

                //填充属性值
                for (int i = 0; i < attributes.getLength(); i++) {
                    String name = attributes.getQName(i);
                    String value = attributes.getValue(i);

                    LiReflectUtil.setFieldValue(saxBean, name, value, true);
                }

                List<String> raw = Lira.of(saxBean.getClass().getFields()).filter(f -> LiReflectUtil.getFieldValue(saxBean, f).absent())
                        .map(Field::getName).getRaw();

                LiAssertUtil.assertTrue(raw.isEmpty(), String.format("parse attributes to bean fields error at  line %d , because  the  %s  is null", locator.getLineNumber(), raw));


                // 将当前 saxBean 压栈，以供后续的深度遍历使用
                stack.push(saxBean);

            }

            @Override
            public void characters(char[] ch, int start, int length) {
                String trim = new String(ch, start, length).trim();
                if (LiStringUtils.isEmpty(trim)) {
                    return;
                }
                LiPrintUtil.print("----", stack.peek(), trim);

            }

            @Override
            public void endElement(String uri, String localName, String qName) {

                // 当前层级遍历完成，弹栈，回到上一层
                SaxBean pop = stack.pop();

                if (stack.isEmpty()) { // 栈空，则表示解析完成
                    LiPrintUtil.print("~~~~~~~~~", pop);
                }

            }
        });
    }
}
