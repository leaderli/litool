package io.leaderli.litool.dom.parser;

import io.leaderli.litool.core.meta.LiBox;
import io.leaderli.litool.core.meta.Lira;
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
    /**
     * 空标签，不实际使用，仅用作标记，表明当前标签不做任何处理
     */
    public static final SaxBean IGNORE_TAG = new SaxBean() {
    };

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

                    if (peek == IGNORE_TAG) {

                        saxBeanStack.push(IGNORE_TAG);
                        continue;
                    }

                    // 默认空标签
                    LiBox<SaxBean> push = LiBox.of(IGNORE_TAG);

                    // 优先查找同名 SaxBean 成员
                    ReflectUtil.getField(peek.getClass(), saxEvent.name).ifPresent(field -> {

                        Class<?> type = field.getType();
                        ReflectUtil.newInstance(type).cast(SaxBean.class)
                                .ifPresent(newFieldBean -> {
                                    ReflectUtil.setFieldValue(peek, field, newFieldBean);
                                    push.value(newFieldBean);
                                });
                    });

                    // 查找 支持该标签的 SaxList 成员
                    if (IGNORE_TAG == push.value()) {

                        Lira.of(peek.getClass().getFields())
                                .map(field -> ReflectUtil.getFieldValue(peek, field).get())
                                .cast(SaxList.class)
                                .filter(saxList ->
                                        saxList.support().getValueByKey(saxEvent.name)
                                )
                                .first()
                                .ifPresent(saxList -> {

                                    Class<SaxBean> saxBeanClass = saxList.support().getValueByKey(saxEvent.name).get();

                                    ReflectUtil.newInstance(saxBeanClass).ifPresent(newFieldBean -> {
                                        saxList.add(newFieldBean);
                                        push.value(newFieldBean);
                                    });

                                });

                    }


                    // 压栈
                    push.lino().assertNotNone("parser error at " + saxEvent.locator.getLineNumber()).ifPresent(saxBeanStack::push);

                }
            } else if (saxEvent instanceof AttributeEvent) {

                SaxBean peek = saxBeanStack.peek();
                if (peek == IGNORE_TAG) {
                    continue;
                }

                // 使用 attribute 的值填充 field 的值
                ReflectUtil.getField(peek.getClass(), saxEvent.name)
                        .ifPresent(field -> {

                            String value = ((AttributeEvent) saxEvent).value;
                            StringConvert.parser(field.getType(), value)
                                    .ifPresent(v -> ReflectUtil.setFieldValue(peek, field, v));

                        });


            } else if (saxEvent instanceof BodyEvent) {

                SaxBean peek = saxBeanStack.peek();
                if (peek == IGNORE_TAG) {
                    continue;
                }
                String value = ((BodyEvent) saxEvent).description();
                Lira.of(peek.getClass().getFields())
                        .filter(f -> f.getType() == SaxBody.class)
                        .forEach(f -> ReflectUtil.setFieldValue(peek, f, new SaxBody(value)));

            } else if (saxEvent instanceof EndEvent) {

                SaxBean pop = saxBeanStack.pop();

                // 校验是否有成员变量未初始化
                Lira.of(pop.getClass().getFields())
                        .forEach(field ->
                                ReflectUtil.getFieldValue(pop, field).assertNotNone(String.format("%s has no init", field))
                        );
            }
        }
        return root;
    }
}
