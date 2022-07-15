package io.leaderli.litool.dom.parser;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.exception.RuntimeExceptionTransfer;
import io.leaderli.litool.core.lang3.LiStringUtils;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.LiReflectUtil;
import io.leaderli.litool.core.util.LiIoUtil;
import io.leaderli.litool.core.util.LiPrintUtil;
import io.leaderli.litool.dom.LocatorDefaultHandler;
import io.leaderli.litool.dom.sax.SaxBean;
import org.xml.sax.Attributes;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Stack;

/**
 * @author leaderli
 * @since 2022/7/7
 */
public class LiDomDFSContext {


    // TODO 父类注册属性
    //  最后注册supportTag
    private static class DFSLocatorHandler<T extends SaxBean> extends LocatorDefaultHandler {

        private final Stack<SaxBean> stack = new Stack<>();
        private final T root;

        private DFSLocatorHandler(T root) {
            this.root = root;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {

            if (stack.isEmpty()) {
                populateProperties(null, root, attributes);
                return;
            }

            SaxBean peek = stack.peek();

            Lino.of(peek.support())
                    .map(pair -> pair.getValueByKey(qName.toLowerCase()).get())
                    .throwable_map(Class::newInstance)
                    .assertNotNone(String.format("not support to handle <%s> in <%s> at line %d , column %d", qName, peek.tagName(), locator.getLineNumber(), locator.getColumnNumber()))
                    .ifPresent(saxBean -> populateProperties(peek, saxBean, attributes));


        }

        private void startAttribute(String name, String value) {
            SaxBean saxBean = stack.peek();
            LiReflectUtil.setFieldValue(saxBean, name, value, true);

        }

        private void populateProperties(SaxBean parent, SaxBean saxBean, Attributes attributes) {

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
            if (!stack.isEmpty()) {
                stack.peek().add(pop);
            }
        }

        public T getResult() {
            return root;
        }
    }

    public static <T extends SaxBean> T parse(String path, Class<T> cls) {

        return parse(LiIoUtil.getResourceAsStream(path), cls);
    }

    public static <T extends SaxBean> T parse(InputStream inputStream, Class<T> cls) {

        return RuntimeExceptionTransfer.get(() -> {

            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            DFSLocatorHandler<T> dh = new DFSLocatorHandler<>(cls.newInstance());
            saxParser.parse(inputStream, dh);
            return dh.getResult();
        });
    }
}
