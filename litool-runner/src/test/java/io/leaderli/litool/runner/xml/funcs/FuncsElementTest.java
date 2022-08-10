package io.leaderli.litool.runner.xml.funcs;

import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.util.ConsoleUtil;
import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import org.junit.jupiter.api.Test;

public class FuncsElementTest {

    @Test
    public void duplicate_name() {
        SaxEventInterceptor<FuncsElement> dfs = new SaxEventInterceptor<>(FuncsElement.class);

        FuncsElement funcsElement = dfs.parse("funcs/funcs_duplicate_name.xml");

        ConsoleUtil.println(dfs.getParseErrorMsgs());

        LiAssertUtil.assertTrue(dfs.getParseErrorMsgs().get(0).startsWith("duplicate name of "));
    }

    @Test
    public void duplicate_label() {
        SaxEventInterceptor<FuncsElement> dfs = new SaxEventInterceptor<>(FuncsElement.class);

        FuncsElement funcsElement = dfs.parse("funcs/funcs_duplicate_label.xml");

        ConsoleUtil.println(dfs.getParseErrorMsgs());

        LiAssertUtil.assertTrue(dfs.getParseErrorMsgs().get(0).startsWith("duplicate label of "));
    }

    @Test
    public void funcList_empty() {
        SaxEventInterceptor<FuncsElement> dfs = new SaxEventInterceptor<>(FuncsElement.class);

        FuncsElement funcsElement = dfs.parse("funcs/funcs_funcList_empty.xml");

        ConsoleUtil.println(dfs.getParseErrorMsgs());

        LiAssertUtil.assertTrue(dfs.getParseErrorMsgs().get(0).startsWith("the funcList of funcs is empty"));
    }

}
