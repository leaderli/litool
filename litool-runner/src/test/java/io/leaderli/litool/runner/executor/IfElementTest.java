package io.leaderli.litool.runner.executor;

import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import io.leaderli.litool.runner.xml.MainElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IfElementTest {

    @Test
    void test() {
        SaxEventInterceptor<MainElement> dfs = new SaxEventInterceptor<>(MainElement.class);
        MainElement element = dfs.parse("if_error.xml");

        Assertions.assertTrue(dfs.getParseErrorMsgs().get(0).startsWith("cond expression [time] only support boolean"));

    }

}
