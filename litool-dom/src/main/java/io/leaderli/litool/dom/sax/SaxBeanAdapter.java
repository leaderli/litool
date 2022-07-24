package io.leaderli.litool.dom.sax;

/**
 * @author leaderli
 * @since 2022/7/24
 */
public class SaxBeanAdapter implements Runnable, SaxEventHandler {


    public final SaxBean sax;
    /**
     * 用于在 {@link EndEvent} 中回调
     */
    private final Runnable callback;
    /**
     * 保存解析的开始事件，用于 {@link EndEvent#getSaxBeanWrapper()#getStartEvent()} 中获取解析开始的位置等
     */
    private StartEvent startEvent;

    private SaxBeanAdapter(SaxBean sax, Runnable callback) {
        this.sax = sax;
        this.callback = callback;
    }

    public static SaxBeanAdapter of(SaxBean sax, Runnable runnable) {
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
        sax.start(startEvent);

    }

    @Override
    public SaxBean sax() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void attribute(AttributeEvent attributeEvent) {
        sax.attribute(attributeEvent);
    }

    @Override
    public void body(BodyEvent bodyEvent) {
        sax.body(bodyEvent);
    }

    @Override
    public void end(EndEvent endEvent) {
        sax.end(endEvent);
    }

    @Override
    public void run() {
        if (this.callback != null) {
            this.callback.run();
        }
    }


}
