package io.leaderli.litool.runner.executor;

import io.leaderli.litool.core.collection.ImmutableMap;
import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import io.leaderli.litool.runner.Context;
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
        context.visit(new FuncsElementExecutor(funcsElement));

        ImmutableMap<String, IFunc> funcContainer = context.getFuncFactory();
        Assertions.assertSame(funcContainer.get("func_a").funcScope, FuncScope.RUNTIME);
        Assertions.assertSame(funcContainer.get("func_b").funcScope, FuncScope.RUNTIME);
        Assertions.assertSame(funcContainer.get("func_c").funcScope, FuncScope.CONTEXT);
        Assertions.assertSame(funcContainer.get("func_d").funcScope, FuncScope.CONSTANT);
    }
}
