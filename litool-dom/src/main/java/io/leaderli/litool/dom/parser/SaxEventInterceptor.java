package io.leaderli.litool.dom.parser;

import io.leaderli.litool.core.exception.RuntimeExceptionTransfer;
import io.leaderli.litool.core.resource.ResourceUtil;
import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.dom.sax.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

/**
 * @author leaderli
 * @since 2022/7/7
 */
public class SaxEventInterceptor<T extends SaxBean> {

    private static final SaxBean DEFAULT_IGNORE_SAX_BEAN = new IgnoreSaxBean();
    private final Class<T> entryClass;
    /**
     * 默认的标签实现类，一般使用 {@link IgnoreSaxBean} , 当有特殊需要时，例如打印所有无具体实现类的标签时可用
     */
    private final SaxBean ignoreSaxBean;

    public SaxEventInterceptor(Class<T> entryClass) {
        this(entryClass, DEFAULT_IGNORE_SAX_BEAN);
    }

    public SaxEventInterceptor(Class<T> entryClass, SaxBean ignoreSaxBean) {
        Objects.requireNonNull(entryClass);
        Objects.requireNonNull(ignoreSaxBean);
        this.entryClass = entryClass;
        this.ignoreSaxBean = ignoreSaxBean;
    }


    public T parse(String path) {
        List<SaxEvent> saxEventList = RuntimeExceptionTransfer.get(() -> getSaxEventList(path));

        T entry = ReflectUtil.newInstance(entryClass).get();

        Stack<SaxBeanAdapter> saxBeanStack = new Stack<>();
        for (SaxEvent saxEvent : saxEventList) {


            if (saxEvent instanceof StartEvent) {


                if (saxBeanStack.isEmpty()) {
                    saxBeanStack.push(SaxBeanAdapter.of(entry));
                } else {


                    SaxBeanAdapter peek = saxBeanStack.peek();
                    StartEvent startEvent = (StartEvent) saxEvent;
                    startEvent.setNewSaxBean(SaxBeanAdapter.of(ignoreSaxBean));
                    peek.start(startEvent);

                    SaxBeanAdapter newSaxBean = startEvent.getNewSaxBean();
                    newSaxBean.origin.setRoot(entry);
                    saxBeanStack.push(newSaxBean);
                }

            } else if (saxEvent instanceof AttributeEvent) {

                SaxBeanAdapter peek = saxBeanStack.peek();
                peek.attribute((AttributeEvent) saxEvent);

            } else if (saxEvent instanceof BodyEvent) {

                SaxBeanAdapter peek = saxBeanStack.peek();

                peek.body((BodyEvent) saxEvent);

            } else if (saxEvent instanceof EndEvent) {

                SaxBeanAdapter pop = saxBeanStack.pop();

                EndEvent endEvent = (EndEvent) saxEvent;
                endEvent.setSaxBeanWrapper(pop);

                pop.end(endEvent);

            }
        }
        return entry;
    }

    private static <T extends SaxBean> List<SaxEvent> getSaxEventList(String path) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        SaxEventLocatorHandler<T> dh = new SaxEventLocatorHandler<>();
        saxParser.parse(ResourceUtil.getResourceAsStream(path), dh);
        return dh.getSaxEventList();
    }


}
