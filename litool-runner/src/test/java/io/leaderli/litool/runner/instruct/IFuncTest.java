package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.core.collection.ImmutableMap;
import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.executor.FuncsElementExecutor;
import io.leaderli.litool.runner.xml.funcs.FuncsElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class IFuncTest {
    @Test
    void context() {
        SaxEventInterceptor<FuncsElement> dfs_funcs = new SaxEventInterceptor<>(FuncsElement.class);
        FuncsElement funcsElement = dfs_funcs.parse("funcs/ifunc/funcs_context.xml");

        Map<String, String> request = new HashMap<>();
        request.put("param1", "1");
        Context context = new Context(request);

        Map<String, Object> request_only = new HashMap<>(request);
        context.setReadonly_request(ImmutableMap.of(request_only));

        context.visit(new FuncsElementExecutor(funcsElement));
        Assertions.assertSame(context._getFuncContainer().get("func_a").funcScope, FuncScope.CONTEXT);
        Assertions.assertNull(context.getFuncResultCache("func_a"));
        Assertions.assertTrue((Boolean) context.getFuncResult("func_a"));
        Assertions.assertNotNull(context.getFuncResultCache("func_a"));
    }

    @Test
    void constant() {
        SaxEventInterceptor<FuncsElement> dfs = new SaxEventInterceptor<>(FuncsElement.class);
        FuncsElement funcsElement = dfs.parse("funcs/ifunc/funcs_constant.xml");

        Map<String, String> request = new HashMap<>();
        Context context = new Context(request);

        FuncsElementExecutor funcsElementExecutor = new FuncsElementExecutor(funcsElement);
        funcsElementExecutor.visit(context);

        Assertions.assertNull(context.getFuncResultCache("func_a"));
        Assertions.assertTrue((Boolean) context.getFuncResult("func_a"));
        Assertions.assertNull(context.getFuncResultCache("func_a"));
    }

    @Test
    void runtime() {
        SaxEventInterceptor<FuncsElement> dfs = new SaxEventInterceptor<>(FuncsElement.class);
        FuncsElement funcsElement = dfs.parse("funcs/ifunc/funcs_runtime.xml");

        Map<String, String> request = new HashMap<>();
        request.put("_testCurrentTime", "");
        request.put("_test", "");
        Context context = new Context(request);

        Map<String, Object> request_only = new HashMap<>(request);
        context.setReadonly_request(ImmutableMap.of(request_only));

        FuncsElementExecutor funcsElementExecutor = new FuncsElementExecutor(funcsElement);
        funcsElementExecutor.visit(context);

        Assertions.assertSame(context._getFuncContainer().get("func_a").funcScope, FuncScope.RUNTIME);
        Assertions.assertNull(context.getFuncResultCache("func_a"));
        Assertions.assertEquals(9, ((String) context.getFuncResult("func_a")).length());
        Assertions.assertNull(context.getFuncResultCache("func_a"));
    }

}