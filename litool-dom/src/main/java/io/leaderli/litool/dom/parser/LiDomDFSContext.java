package io.leaderli.litool.dom.parser;

import io.leaderli.litool.core.lang.TupleMap;
import io.leaderli.litool.core.meta.LiBox;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.resource.ResourceUtil;
import io.leaderli.litool.core.text.StringConvert;
import io.leaderli.litool.core.type.ClassUtil;
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
    public static final SaxBeanWrapper IGNORE_TAG = SaxBeanWrapper.of(null);

    @SuppressWarnings("unchecked")
    public static <T extends SaxBean> T parse(String path, Class<T> cls) throws ParserConfigurationException, SAXException, IOException {


        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        DFSLocatorHandler<T> dh = new DFSLocatorHandler<>();
        saxParser.parse(ResourceUtil.getResourceAsStream(path), dh);

        T root = ReflectUtil.newInstance(cls).get();
        Stack<SaxBeanWrapper> saxBeanStack = new Stack<>();
        Map<Integer, String> map = ResourceUtil.lineStrOfResourcesFile(path);
        for (SaxEvent saxEvent : dh.getSaxEventList()) {
            if (saxEvent instanceof StartEvent) {

                if (saxBeanStack.isEmpty()) {
                    saxBeanStack.push(SaxBeanWrapper.of(root));
                } else {


                    if (ignore(saxBeanStack)) {

                        saxBeanStack.push(IGNORE_TAG);
                        continue;
                    }

                    SaxBean peek = saxBeanStack.peek().sax;

                    // 默认空标签
                    LiBox<SaxBeanWrapper> push = LiBox.of(IGNORE_TAG);

                    // 优先查找同名 SaxBean 成员
                    ReflectUtil.getField(peek.getClass(), saxEvent.name).ifPresent(field -> {

                        Class<?> type = field.getType();
                        ReflectUtil.newInstance(type)
                                .cast(SaxBean.class)
                                .ifPresent(newFieldBean -> push.value(SaxBeanWrapper.of(
                                                newFieldBean,
                                                () -> ReflectUtil.setFieldValue(peek, field, newFieldBean))
                                        )
                                );
                    });

                    // 若未查找到，则查找支持该标签的 SaxList 成员
                    if (IGNORE_TAG == push.value()) {

                        Lira.of(peek.getClass().getFields())
                                .map(field -> ReflectUtil.getFieldValue(peek, field).get())
                                .cast(SaxList.class)
                                .filter(saxList -> saxList.support().getValueByKey(saxEvent.name))
                                .first()
                                .ifPresent(saxList -> {

                                    TupleMap<String, Class<?>> support = saxList.support();

                                    support.getValueByKey(saxEvent.name)
                                            .map(ReflectUtil::newInstance)
                                            .map(Lino::get)
                                            .cast(SaxBean.class)
                                            .ifPresent(newFieldBean ->
                                                    push.value(SaxBeanWrapper.of(
                                                            newFieldBean,
                                                            () -> saxList.add(newFieldBean)))
                                            );

                                });

                    }


                    // 压栈
                    push.lino().assertNotNone("parser error at " + saxEvent.locator.getLineNumber()).ifPresent(saxBeanStack::push);

                }
            } else if (saxEvent instanceof AttributeEvent) {

                if (ignore(saxBeanStack)) {
                    continue;
                }
                SaxBean peek = saxBeanStack.peek().sax;

                // 使用 attribute 的值填充 field 的值
                ReflectUtil.getField(peek.getClass(), saxEvent.name)
                        .ifPresent(field -> {
                            Object fieldValue;
                            String value = ((AttributeEvent) saxEvent).value;
                            if (StringConvert.support(field.getType())) {

                                fieldValue = StringConvert.parser(field.getType(), value).get();
                            } else {
                                fieldValue = ReflectUtil.newInstance(field.getType(), value).get();
                            }

                            ReflectUtil.setFieldValue(peek, field, fieldValue);

                        });


            } else if (saxEvent instanceof BodyEvent) {

                if (ignore(saxBeanStack)) {

                    continue;
                }
                SaxBean peek = saxBeanStack.peek().sax;
                String value = ((BodyEvent) saxEvent).description();
                Lira.of(peek.getClass().getFields())
                        .filter(field -> ClassUtil.isAssignableFromOrIsWrapper(SaxBody.class, field.getType()))
                        .first()
                        .ifPresent(field -> {
                                    Object fieldValue = ReflectUtil.newInstance(field.getType(), value).get();
                                    ReflectUtil.setFieldValue(peek, field, fieldValue);
                                }
                        );

            } else if (saxEvent instanceof EndEvent) {

                SaxBeanWrapper callback = saxBeanStack.pop();
                //在 end 时，子元素已经加载完毕， 此时将子元素填充到父元素
                callback.run();
                SaxBean pop = callback.sax;
                if (pop == null) {
                    continue;
                }
                // 校验是否有成员变量未初始化
                Lira.of(pop.getClass().getFields())
                        .forEach(field ->
                                ReflectUtil.getFieldValue(pop, field).assertNotNone(String.format("%s has no init", field))
                        );

            }
        }
        return root;
    }

    public static boolean ignore(Stack<SaxBeanWrapper> stack) {
        return stack.peek() == IGNORE_TAG;
    }
}
