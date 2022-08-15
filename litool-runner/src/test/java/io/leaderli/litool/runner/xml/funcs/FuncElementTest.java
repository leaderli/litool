package io.leaderli.litool.runner.xml.funcs;

import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import io.leaderli.litool.runner.Context;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

/**
 * @author leaderli
 * @since 2022/8/9
 */
class FuncElementTest {

    @Test
    void error() {
        SaxEventInterceptor<FuncElement> dfs = new SaxEventInterceptor<>(FuncElement.class);

        FuncElement funcElement = dfs.parse("funcs/func_error.xml");

        Assertions.assertTrue(dfs.getParseErrorMsgs().get(0).startsWith("the func name [$switch86] is not match [a-zA-Z0-9_]+ at"));
        Assertions.assertTrue(dfs.getParseErrorMsgs().get(1).startsWith("instruct [$1] is unsupported at "));

    }

    @Test
    void param_error() {
        SaxEventInterceptor<FuncElement> dfs = new SaxEventInterceptor<>(FuncElement.class);

        FuncElement funcElement = dfs.parse("funcs/func_param_error.xml");


        Assertions.assertTrue(dfs.getParseErrorMsgs().get(0).startsWith("the func [switch86] parameterType is  not match clazz [in] parameterType "));

    }


    @Test
    void param_type_error() {
        SaxEventInterceptor<FuncsElement> dfs = new SaxEventInterceptor<>(FuncsElement.class);

        FuncsElement funcElement = dfs.parse("funcs/func_param_type_error.xml");

        Context context = new Context(new HashMap<>());
funcElement.executor().visit(context);

        Assertions.assertTrue(dfs.getParseErrorMsgs().get(0).startsWith("false cannot parse to int"));

    }
}
