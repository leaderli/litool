package io.leaderli.litool.runner.executor;

import io.leaderli.litool.core.collection.ImmutableMap;
import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import io.leaderli.litool.runner.Context;
import io.leaderli.litool.runner.executor.funcs.FuncsElementExecutor;
import io.leaderli.litool.runner.instruct.FuncScope;
import io.leaderli.litool.runner.instruct.IFunc;
import io.leaderli.litool.runner.xml.funcs.FuncsElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

class FuncsElementExecutorTest {

    @Test
    void funcScope() {
        SaxEventInterceptor<FuncsElement> dfs = new SaxEventInterceptor<>(FuncsElement.class);
        FuncsElement funcsElement = dfs.parse("funcs/funcs_funcScope.xml");

        Context context = new Context(new HashMap<>());
        new FuncsElementExecutor(funcsElement).visit(context);

        ImmutableMap<String, IFunc> funcContainer = context.getFuncFactory();
        Assertions.assertSame(FuncScope.RUNTIME, funcContainer.get("func_a").funcScope);
        Assertions.assertSame(FuncScope.RUNTIME, funcContainer.get("func_b").funcScope);
        Assertions.assertSame(FuncScope.CONTEXT, funcContainer.get("func_c").funcScope);
        Assertions.assertSame(FuncScope.CONSTANT, funcContainer.get("func_d").funcScope);
    }
}
