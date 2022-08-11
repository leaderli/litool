package io.leaderli.litool.runner.xml.funcs;

import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FuncsElementTest {

    @Test
    public void duplicate_name() {
        SaxEventInterceptor<FuncsElement> dfs = new SaxEventInterceptor<>(FuncsElement.class);
        FuncsElement funcsElement = dfs.parse("funcs/funcs_duplicate_name.xml");
        Assertions.assertTrue(dfs.getParseErrorMsgs().get(0).startsWith("duplicate name of "));
    }

    @Test
    public void duplicate_label() {
        SaxEventInterceptor<FuncsElement> dfs = new SaxEventInterceptor<>(FuncsElement.class);
        FuncsElement funcsElement = dfs.parse("funcs/funcs_duplicate_label.xml");
        Assertions.assertTrue(dfs.getParseErrorMsgs().get(0).startsWith("duplicate label of "));
    }

    @Test
    public void funcList_empty() {
        SaxEventInterceptor<FuncsElement> dfs = new SaxEventInterceptor<>(FuncsElement.class);
        FuncsElement funcsElement = dfs.parse("funcs/funcs_funcList_empty.xml");
        Assertions.assertTrue(dfs.getParseErrorMsgs().get(0).startsWith("the funcList of funcs is empty"));
    }

    @Test
    public void circular_reference() {
        SaxEventInterceptor<FuncsElement> dfs = new SaxEventInterceptor<>(FuncsElement.class);
        FuncsElement funcsElement = dfs.parse("funcs/funcs_circular_reference.xml");
        Assertions.assertTrue(dfs.getParseErrorMsgs().get(0).startsWith("error reference"));
    }

}
