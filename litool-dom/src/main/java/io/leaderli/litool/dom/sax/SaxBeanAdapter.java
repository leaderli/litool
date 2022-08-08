package io.leaderli.litool.dom.sax;

import io.leaderli.litool.core.exception.ExceptionUtil;
import org.xml.sax.Locator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leaderli
 * @since 2022/7/24
 */
public class SaxBeanAdapter implements Runnable, SaxEventHandler {


    public final SaxBean origin;
    /**
     * 用于在 {@link EndEvent} 中回调，用来执行参数校验，赋值复杂元素等
     */
    private final List<Runnable> callbacks = new ArrayList<>();

    private final List<String> parseErrorMsgs = new ArrayList<>();
//    /**
//     * 保存解析的开始事件，用于 {@link EndEvent#getSaxBeanWrapper()#getStartEvent()} 中获取解析开始的位置等
//     */
//    private StartEvent startEvent;

    private SaxBeanAdapter(SaxBean origin) {
        this.origin = origin;
    }


    public static SaxBeanAdapter of(SaxBean sax) {
        return new SaxBeanAdapter(sax);
    }


    public void addCallback(Runnable callback) {
        this.callbacks.add(callback);
    }

    @Override
    public void start(StartEvent startEvent) {
        try {

            origin.start(startEvent);
        } catch (Throwable throwable) {
            Throwable cause = ExceptionUtil.getCause(throwable);
            Locator locator = startEvent.locator;
            parseErrorMsgs.add(String.format("%s at line:%d column:%d", cause.getMessage(), locator.getLineNumber(), locator.getColumnNumber()));
        }
    }

    @Override
    public void attribute(AttributeEvent attributeEvent) {
        try {

            origin.attribute(attributeEvent);
        } catch (Throwable throwable) {
            Throwable cause = ExceptionUtil.getCause(throwable);
            Locator locator = attributeEvent.locator;
            parseErrorMsgs.add(String.format("%s at line:%d column:%d", cause.getMessage(), locator.getLineNumber(), locator.getColumnNumber()));
        }
    }

    @Override
    public void body(BodyEvent bodyEvent) {
        try {

            origin.body(bodyEvent);
        } catch (Throwable throwable) {
            Throwable cause = ExceptionUtil.getCause(throwable);
            Locator locator = bodyEvent.locator;
            parseErrorMsgs.add(String.format("%s at line:%d column:%d", cause.getMessage(), locator.getLineNumber(), locator.getColumnNumber()));
        }
    }

    @Override
    public void end(EndEvent endEvent) {
        try {
            origin.end(endEvent);
        } catch (Throwable throwable) {
            Throwable cause = ExceptionUtil.getCause(throwable);
            Locator locator = endEvent.locator;
            parseErrorMsgs.add(String.format("%s at line:%d column:%d", cause.getMessage(), locator.getLineNumber(), locator.getColumnNumber()));
        }
    }


    @Override
    public void run() {

        for (Runnable callback : this.callbacks) {
            callback.run();
        }
    }



//    public void setStartEvent(StartEvent startEvent) {
//        this.startEvent = startEvent;
//    }

    public List<String> getParseErrorMsgs() {
        return parseErrorMsgs;
    }

    @Override
    public String toString() {
        return "SaxBeanAdapter{" +
                "origin=" + origin +
                '}';
    }
}
