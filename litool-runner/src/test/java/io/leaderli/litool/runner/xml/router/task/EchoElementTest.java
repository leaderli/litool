package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.core.event.ILiEventListener;
import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.executor.EchoElementExecutor;
import io.leaderli.litool.runner.xml.MainElement;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class EchoElementTest {

    @Test
    void success() {
        SaxEventInterceptor<MainElement> dfs = new SaxEventInterceptor<>(MainElement.class);

        MainElement mainElement = dfs.parse("router/task/echo.xml");

        Context context = new Context(new HashMap<>());
        context.setResponse("Code","114514");
        context.setTemp("coordinate","你好");
        context.registerListener(new ILiEventListener<EchoElementExecutor.EchoEvent>() {
            @Override
            public void listen(EchoElementExecutor.EchoEvent source) {
                System.out.println(source.getSource().get());
            }

            @Override
            public Class<EchoElementExecutor.EchoEvent> componentType() {
                return EchoElementExecutor.EchoEvent.class;
            }
        });
        mainElement.executor().visit(context);
        LiAssertUtil.assertTrue(StringUtils.startsWith(dfs.getParseErrorMsgs().get(0),"response variable [Code] not exists"));
    }
}
