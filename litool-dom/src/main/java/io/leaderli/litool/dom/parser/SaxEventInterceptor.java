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
import java.util.ArrayList;
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
        List<SaxEvent> saxEventList = RuntimeExceptionTransfer.get(() -> getSaxEventList(path));

        T root = ReflectUtil.newInstance(entryClass).get();

        Stack<SaxBeanAdapter> saxBeanStack = new Stack<>();
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
                    newSaxBean.origin.setRoot(root);
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

                parseErrorMsgs.addAll(pop.getParseErrorMsgs());

            }
        }
        return root;
    }

    public List<String> getParseErrorMsgs() {
        return parseErrorMsgs;
    }


    private static <T extends SaxBean> List<SaxEvent> getSaxEventList(String path) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        SaxEventLocatorHandler<T> dh = new SaxEventLocatorHandler<>();
        saxParser.parse(ResourceUtil.getResourceAsStream(path), dh);
        return dh.getSaxEventList();
    }


}
