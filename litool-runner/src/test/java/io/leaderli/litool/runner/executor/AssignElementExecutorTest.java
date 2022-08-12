package io.leaderli.litool.runner.executor;

import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.xml.router.task.AssignElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class AssignElementExecutorTest {

    @Test
    void visit() {
        SaxEventInterceptor<AssignElement> dfs = new SaxEventInterceptor<>(AssignElement.class);
        AssignElement element = dfs.parse("router/task/assign.xml");

        Map<String, String> request = new HashMap<>();
        Context context = new Context(request);
        context.visit(new AssignElementExecutor(element));

        Assertions.assertTrue(StringUtils.equals(context.getResponse("skill"), "123"));
    }
}