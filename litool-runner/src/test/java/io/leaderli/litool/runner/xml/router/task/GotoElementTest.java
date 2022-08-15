package io.leaderli.litool.runner.xml.router.task;

import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.xml.MainElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/8/15 12:49 PM
 */
class GotoElementTest {
    @Test
    void test() {
        SaxEventInterceptor<MainElement> dfs = new SaxEventInterceptor<>(MainElement.class);
        MainElement element = dfs.parse("router/task/goto_error.xml");

        Map<String, String> request = new HashMap<>();

        Context context = new Context(request);
        element.executor().visit0(context);

        Assertions.assertTrue(dfs.getParseErrorMsgs().get(0).contains("next has no init"));
        Assertions.assertTrue(dfs.getParseErrorMsgs().get(1).startsWith("goto next [001] is not exist"));
    }
}
