package io.leaderli.litool.dom.parser;

import io.leaderli.litool.core.resource.ResourceUtil;
import io.leaderli.litool.core.type.ReflectUtil;
import io.leaderli.litool.dom.sax.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author leaderli
 * @since 2022/7/7
 */
public class SaxEventInterceptor<T extends SaxBean> {

    private static final SaxBean DEFAULT_IGNORE_SAX_BEAN = new IgnoreSaxBean();
    private final Class<T> entryClass;
    /**
     * 默认的标签实现类，一般使用 {@link IgnoreSaxBean} , 当有特殊需要时，例如打印所有无具体实现类的标签时可用。
     * 有效标签会替换该默认实现
     *
     * @see SaxEventHandler#start(StartEvent)
     * @see StartEvent#setNewSaxBean(SaxBeanAdapter)
     */
    private final SaxBean ignoreSaxBean;
    private final List<String> parseErrorMsgs = new ArrayList<>();

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
        return parse(ResourceUtil.getResourceAsStream(path));
    }

    public T parse(InputStream xmlStream) {

        List<SaxEvent> saxEventList;
        try {
            saxEventList = getSaxEventList(xmlStream);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }

        T root = ReflectUtil.newInstance(entryClass).get();

        Deque<SaxBeanAdapter> saxBeanStack = new ArrayDeque<>();
        for (SaxEvent saxEvent : saxEventList) {


            if (saxEvent instanceof StartEvent) {


                if (saxBeanStack.isEmpty()) {
                    saxBeanStack.push(SaxBeanAdapter.of(root));
                } else {


                    SaxBeanAdapter peek = saxBeanStack.peek();
                    StartEvent startEvent = (StartEvent) saxEvent;
                    // 设置默认空标签
                    startEvent.setNewSaxBean(SaxBeanAdapter.of(ignoreSaxBean));
                    peek.start(startEvent);

                    // 实际解析后的有效标签
                    SaxBeanAdapter newSaxBean = startEvent.getNewSaxBean();
                    saxBeanStack.push(newSaxBean);
                }

            } else if (saxEvent instanceof AttributeEvent) {

                SaxBeanAdapter peek = saxBeanStack.peek();
                assert peek != null;
                peek.attribute((AttributeEvent) saxEvent);

            } else if (saxEvent instanceof BodyEvent) {

                SaxBeanAdapter peek = saxBeanStack.peek();

                assert peek != null;
                peek.body((BodyEvent) saxEvent);

            } else if (saxEvent instanceof EndEvent) {

                SaxBeanAdapter pop = saxBeanStack.pop();

                EndEvent endEvent = (EndEvent) saxEvent;
                endEvent.setSaxBeanWrapper(pop);

                pop.end(endEvent);

                parseErrorMsgs.addAll(pop.getParseErrorMsgs());

            }
        }
        return root;
    }


    private static List<SaxEvent> getSaxEventList(InputStream xmlStream) throws ParserConfigurationException,
            SAXException, IOException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        SAXParser saxParser = saxParserFactory.newSAXParser();
        SaxEventLocatorHandler dh = new SaxEventLocatorHandler();

        saxParser.parse(xmlStream, dh);
        return dh.getSaxEventList();
    }

    public List<String> getParseErrorMsgs() {
        return parseErrorMsgs;
    }
}
