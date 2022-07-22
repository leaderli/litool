package io.leaderli.litool.dom.parser;

import io.leaderli.litool.core.resource.ResourceUtil;
import io.leaderli.litool.core.text.StringConvert;
import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.dom.sax.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.Map;
import java.util.Stack;

/**
 * @author leaderli
 * @since 2022/7/7
 */
public class LiDomDFSContext {


    public static final String CHILDREN_FIELD_NAME = "children";

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends SaxBean> T parse(String path, Class<T> cls) throws ParserConfigurationException, SAXException, IOException {


        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        DFSLocatorHandler<T> dh = new DFSLocatorHandler<>();
        saxParser.parse(ResourceUtil.getResourceAsStream(path), dh);

        T root = ReflectUtil.newInstance(cls).get();
        Stack<SaxBean> saxBeanStack = new Stack<>();
        Map<Integer, String> map = ResourceUtil.lineStrOfResourcesFile(path);
        for (SaxEvent saxEvent : dh.getSaxEventList()) {
            if (saxEvent instanceof StartEvent) {

                if (saxBeanStack.isEmpty()) {

                    saxBeanStack.push(root);

                } else {

                    SaxBean peek = saxBeanStack.peek();
                    // 子节点统一放在 children 属性中
                    peek.support().getValueByKey(saxEvent.name)
                            .ifPresent(tagClass -> {
                                SaxBean tagBean = ReflectUtil.newInstance(tagClass).get();
                                saxBeanStack.push(tagBean);
                                peek.add(tagBean);
                            });
                }
            } else if (saxEvent instanceof AttributeEvent) {

                SaxBean peek = saxBeanStack.peek();

                // 使用 attribute 的值填充 field 的值
                ReflectUtil.getField(peek.getClass(), saxEvent.name)
                        .ifPresent(field -> {

                            String value = ((AttributeEvent) saxEvent).value;
                            StringConvert.parser(field.getType(), value)
                                    .ifPresent(v -> ReflectUtil.setFieldValue(peek, field, v));

                        });


            } else if (saxEvent instanceof BodyEvent) {

                SaxBean peek = saxBeanStack.peek();
                peek.body = ((BodyEvent) saxEvent).description();

            } else if (saxEvent instanceof EndEvent) {

                saxBeanStack.pop();
            }
        }
        return root;
    }
}
