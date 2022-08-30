package io.leaderli.litool.dom.sax;

import io.leaderli.litool.core.exception.ExceptionUtil;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.text.StringUtils;
import org.xml.sax.Locator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author leaderli
 * @since 2022/7/24
 */
public class SaxBeanAdapter implements Runnable, SaxEventHandler {


    @SuppressWarnings("all")
    public static Consumer<Throwable> WHEN_THROWS;
    public final SaxBean origin;
    /**
     * 用于在 {@link EndEvent} 中回调，用来执行参数校验，赋值复杂元素等
     */
    private final List<Runnable> callbacks = new ArrayList<>();
    private final List<String> parseErrorMsgs = new ArrayList<>();

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
        parseErrorInvocationHandler(startEvent, origin::start);
    }

    /**
     * 包裹 saxEvent 执行的代理方法，用于捕捉解析异常信息。
     *
     * @param saxEvent sax事件
     * @param consumer sax事件对应的消费者
     * @param <T>      泛型
     */
    public <T extends SaxEvent> void parseErrorInvocationHandler(T saxEvent, Consumer<T> consumer) {
        try {
            consumer.accept(saxEvent);
        } catch (Throwable throwable) {

            if (WHEN_THROWS != null) {
                WHEN_THROWS.accept(throwable);
            }
            Throwable cause = ExceptionUtil.getCause(throwable);
            Locator locator = saxEvent.locator;

            String id = origin.getId();
            id = Lino.of(id).filter(StringUtils::isNotBlank).map(i -> " id:" + i).get("");
            parseErrorMsgs.add(String.format("%s at line:%d column:%d%s", cause.getMessage(), locator.getLineNumber(),
                    locator.getColumnNumber(), id));
        }
    }

    @Override
    public void attribute(AttributeEvent attributeEvent) {
        parseErrorInvocationHandler(attributeEvent, origin::attribute);

    }

    @Override
    public void body(BodyEvent bodyEvent) {
        parseErrorInvocationHandler(bodyEvent, origin::body);
    }

    @Override
    public void end(EndEvent endEvent) {
        parseErrorInvocationHandler(endEvent, origin::end);
    }


    @Override
    public void run() {

        for (Runnable callback : this.callbacks) {
            callback.run();
        }
    }


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
