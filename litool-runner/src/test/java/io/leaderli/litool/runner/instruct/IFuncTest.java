package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.core.collection.ImmutableMap;
import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.executor.funcs.FuncsElementExecutor;
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

        new FuncsElementExecutor(funcsElement).visit(context);
        Assertions.assertSame(FuncScope.CONTEXT, context.getFuncFactory().get("func_a").funcScope);
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
        funcsElementExecutor.execute(context);

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
        funcsElementExecutor.execute(context);

        Assertions.assertSame(FuncScope.RUNTIME, context.getFuncFactory().get("func_a").funcScope);
        Assertions.assertNull(context.getFuncResultCache("func_a"));
        Assertions.assertEquals(8, ((String) context.getFuncResult("func_a")).length());
        Assertions.assertNull(context.getFuncResultCache("func_a"));
    }

}
