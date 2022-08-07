package io.leaderli.litool.dom.sax;

import io.leaderli.litool.core.exception.ExceptionUtil;
import io.leaderli.litool.core.function.ThrowableRunner;
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
    private final List<ThrowableRunner> callbacks = new ArrayList<>();

    private final List<String> parseErrorMsgs = new ArrayList<>();
    /**
     * 保存解析的开始事件，用于 {@link EndEvent#getSaxBeanWrapper()#getStartEvent()} 中获取解析开始的位置等
     */
    private StartEvent startEvent;

    private SaxBeanAdapter(SaxBean origin) {
        this.origin = origin;
    }


    public static SaxBeanAdapter of(SaxBean sax) {
        return new SaxBeanAdapter(sax);
    }


    public void addCallback(ThrowableRunner callback) {
        this.callbacks.add(callback);
    }

    @Override
    public void start(StartEvent startEvent) {
        origin.start(startEvent);

    }

    @Override
    public void attribute(AttributeEvent attributeEvent) {
        origin.attribute(attributeEvent);
    }

    @Override
    public void body(BodyEvent bodyEvent) {
        origin.body(bodyEvent);
    }

    @Override
    public void end(EndEvent endEvent) {
        origin.end(endEvent);
    }

    @Override
    public void run() {

        for (ThrowableRunner callback : this.callbacks) {

            try {
                callback.run();
            } catch (Throwable throwable) {
                Throwable cause = ExceptionUtil.getCause(throwable);
                Locator locator = this.getStartEvent().locator;
                parseErrorMsgs.add(String.format("%s at line:%d column:%d", cause.getMessage(), locator.getLineNumber(), locator.getColumnNumber()));
            }
        }
    }

    public StartEvent getStartEvent() {
        return startEvent;
    }

    public void setStartEvent(StartEvent startEvent) {
        this.startEvent = startEvent;
    }

    public List<String> getParseErrorMsgs() {
        return parseErrorMsgs;
    }

    @Override
    public String toString() {
        return "SaxBeanAdapter{" +
                "origin=" + origin +
                ", startEvent=" + startEvent +
                '}';
    }
}
