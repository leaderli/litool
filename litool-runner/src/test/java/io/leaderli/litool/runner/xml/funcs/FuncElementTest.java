package io.leaderli.litool.runner.xml.funcs;

import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
}
