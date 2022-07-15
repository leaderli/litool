package io.leaderli.litool.dom.parser;

import io.leaderli.litool.core.lang3.LiStringUtils;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.dom.LocatorDefaultHandler;
import io.leaderli.litool.dom.sax.*;
import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author leaderli
 * @since 2022/7/16
 */
// TODO 父类注册属性
//  最后注册supportTag
public class DFSLocatorHandler<T extends SaxBean> extends LocatorDefaultHandler {

    private final Stack<SaxBean> stack = new Stack<>();

    private final List<SaxEvent> saxEventList = new ArrayList<>();


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        saxEventList.add(new StartEvent(locator, qName));

        for (int i = 0; i < attributes.getLength(); i++) {
            String name = attributes.getQName(i);
            String value = attributes.getValue(i);
            saxEventList.add(new AttributeEvent(locator, name, value));
        }

//            if (stack.isEmpty()) {
//                populateProperties(root, attributes);
//                return;
//            }

//            SaxBean peek = stack.peek();

//            Lino.of(peek.support())
//                    .map(pair -> pair.getValueByKey(qName.toLowerCase()).get())
//                    .throwable_map(Class::newInstance)
//                    .assertNotNone(String.format("not support to handle <%s> in <%s> at line %d , column %d", qName, peek.tagName(), locator.getLineNumber(), locator.getColumnNumber()))
//                    .ifPresent(saxBean -> populateProperties(saxBean, attributes));


    }

//        private void startAttribute(String name, String value) {
//            SaxBean saxBean = stack.peek();
//            LiReflectUtil.setFieldValue(saxBean, name, value, true);
//
//        }

    //        private void populateProperties(SaxBean saxBean, Attributes attributes) {
//            // 将当前 saxBean 压栈，以供后续的深度遍历使用
//            stack.push(saxBean);
//            //填充属性值
//            for (int i = 0; i < attributes.getLength(); i++) {
//                String name = attributes.getQName(i);
//                String value = attributes.getValue(i);
//                startAttribute(name, value);
//
//            }
//
//
//        }
    boolean isSpaceOnly(String bodyStr) {
        String bodyTrimmed = bodyStr.trim();
        return (bodyTrimmed.length() == 0);
    }

    Lino<BodyEvent> getLastEvent() {
        return Lira.of(saxEventList).first().cast(BodyEvent.class);
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        String trim = new String(ch, start, length).trim();

        if (LiStringUtils.isEmpty(trim)) {
            return;
        }


        // 多个连续 body 片段合为一起
        Lira.of(saxEventList)
                .last()
                .cast(BodyEvent.class)
                .or(() -> {
                    BodyEvent bodyEvent = new BodyEvent(locator);
                    saxEventList.add(bodyEvent);
                    return bodyEvent;
                })
                .ifPresent(body -> body.append(trim));
    }

    @Override
    public void endElement(String uri, String localName, String qName) {

        saxEventList.add(new EndEvent(locator, qName));

        // 当前层级遍历完成，弹栈，回到上一层
//            SaxBean pop = stack.pop();
//            if (!stack.isEmpty()) {
//                stack.peek().add(pop);
//            }
    }

    public List<SaxEvent> getSaxEventList() {
        return new ArrayList<>(saxEventList);
    }
}
