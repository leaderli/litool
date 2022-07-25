package io.leaderli.litool.dom.sax;

import io.leaderli.litool.core.function.ThrowableRunner;
import org.xml.sax.Locator;

/**
 * @author leaderli
 * @since 2022/7/24
 */
public class SaxBeanAdapter implements Runnable, SaxEventHandler {


    public final SaxBean origin;
    /**
     * 用于在 {@link EndEvent} 中回调
     */
    private final ThrowableRunner callback;
    /**
     * 保存解析的开始事件，用于 {@link EndEvent#getSaxBeanWrapper()#getStartEvent()} 中获取解析开始的位置等
     */
    private StartEvent startEvent;

    private SaxBeanAdapter(SaxBean origin, ThrowableRunner callback) {
        this.origin = origin;
        this.callback = callback;
    }

    public static SaxBeanAdapter of(SaxBean sax, ThrowableRunner runnable) {
        return new SaxBeanAdapter(sax, runnable);
    }

    public static SaxBeanAdapter of(SaxBean sax) {
        return new SaxBeanAdapter(sax, null);
    }

    public StartEvent getStartEvent() {
        return startEvent;
    }

    public void setStartEvent(StartEvent startEvent) {
        this.startEvent = startEvent;
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
        if (this.callback != null) {
            try {
                this.callback.run();
            } catch (Throwable throwable) {
                Locator locator = this.getStartEvent().locator;
                throw new IllegalStateException(String.format("%s at line:%d column:%d", throwable, locator.getLineNumber(), locator.getColumnNumber()));
            }
        }
    }


}
