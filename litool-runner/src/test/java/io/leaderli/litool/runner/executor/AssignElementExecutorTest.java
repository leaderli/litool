package io.leaderli.litool.runner.executor;

import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.executor.router.task.AssignElementExecutor;
import io.leaderli.litool.runner.xml.MainElement;
import io.leaderli.litool.runner.xml.router.task.AssignElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class AssignElementExecutorTest {




    @Test
    void visit() {
        SaxEventInterceptor<AssignElement> dfs = new SaxEventInterceptor<>(AssignElement.class);
        AssignElement element = dfs.parse("router/task/assign.xml");

        Map<String, String> request = new HashMap<>();
        Context context = new Context(request);
        new AssignElementExecutor(element).visit0(context);

        Assertions.assertTrue(StringUtils.equals(context.getResponse("skill"), "123"));
    }

    @Test
    void name_error() {
        SaxEventInterceptor<MainElement> dfs = new SaxEventInterceptor<>(MainElement.class);
        MainElement element = dfs.parse("router/task/assign_error_name.xml");

        List<String> list = new ArrayList<>();
        element.end_check(list);
        Assertions.assertTrue(StringUtils.equals(list.get(0), "response variable [abc] not exists"));
    }
}
