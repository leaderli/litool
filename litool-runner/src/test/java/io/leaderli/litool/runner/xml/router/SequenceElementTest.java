package io.leaderli.litool.runner.xml.router;

import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import io.leaderli.litool.runner.xml.MainElement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SequenceElementTest {

    @Test
    void duplicate_unit() {
        SaxEventInterceptor<MainElement> dfs = new SaxEventInterceptor<>(MainElement.class);
        MainElement element = dfs.parse("router/unit_label_duplicate.xml");

        System.out.println(dfs.getParseErrorMsgs());
        assertTrue(StringUtils.startsWith(dfs.getParseErrorMsgs().get(0), "duplicate label of"));
    }

}
