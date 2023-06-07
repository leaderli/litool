package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.core.event.ILiEventListener;
import io.leaderli.litool.core.meta.LiBox;
import io.leaderli.litool.core.meta.LiTuple;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.event.EchoEvent;
import io.leaderli.litool.runner.xml.MainElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

class EchoElementTest {



    @Test
    void success() {
        SaxEventInterceptor<MainElement> dfs = new SaxEventInterceptor<>(MainElement.class);

        MainElement mainElement = dfs.parse("router/task/echo.xml");

        Context context = new Context(new HashMap<>());
        context.setResponse("Code", "114514");
        context.setTemp("coordinate", "你好");
        LiBox<LiTuple<Integer, String>> echos = LiBox.none();
        context.registerListener(new ILiEventListener<EchoEvent, LiTuple<Integer, String>>() {
            @Override
            public void listen(LiTuple<Integer, String> source) {
                echos.value(source);

            }

            @Override
            public void onNull() {
                System.out.println("--------------");
                ILiEventListener.super.onNull();
            }

            @Override
            public Class<EchoEvent> componentType() {
                return EchoEvent.class;
            }
        });
        mainElement.executor().visit(context);
        Assertions.assertTrue(StringUtils.startsWith(dfs.getParseErrorMsgs().get(0), "response variable [Code] not " +
                "exists"));

        Assertions.assertEquals("(1, hello 123 world 你好)", echos.value().toString());


    }
}
