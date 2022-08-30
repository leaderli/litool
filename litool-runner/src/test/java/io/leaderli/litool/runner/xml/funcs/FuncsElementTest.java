package io.leaderli.litool.runner.xml.funcs;

import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("java:S5976")
class FuncsElementTest {


    @Test
    void duplicate_name() {
        SaxEventInterceptor<FuncsElement> dfs = new SaxEventInterceptor<>(FuncsElement.class);
        FuncsElement funcsElement = dfs.parse("funcs/funcs_duplicate_name.xml");
        Assertions.assertTrue(dfs.getParseErrorMsgs().get(0).startsWith("duplicate name of "));
    }

    @Test
    void duplicate_label() {
        SaxEventInterceptor<FuncsElement> dfs = new SaxEventInterceptor<>(FuncsElement.class);
        FuncsElement funcsElement = dfs.parse("funcs/funcs_duplicate_label.xml");
        Assertions.assertTrue(dfs.getParseErrorMsgs().get(0).startsWith("duplicate label of "));
    }


    @Test
    void circular_reference() {
        SaxEventInterceptor<FuncsElement> dfs = new SaxEventInterceptor<>(FuncsElement.class);
        FuncsElement funcsElement = dfs.parse("funcs/funcs_circular_reference.xml");
        Assertions.assertTrue(dfs.getParseErrorMsgs().get(0).startsWith("error reference"));
    }

}
