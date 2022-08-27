package io.leaderli.litool.runner.xml.router;

import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import io.leaderli.litool.runner.xml.MainElement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RouterElementTest {

@Test
void duplicate_name() {
    SaxEventInterceptor<MainElement> dfs = new SaxEventInterceptor<>(MainElement.class);
    MainElement element = dfs.parse("router/sequence_name_duplicate.xml");

    assertTrue(StringUtils.startsWith(dfs.getParseErrorMsgs().get(0), "duplicate name of"));
}

@Test
void duplicate_label() {
    SaxEventInterceptor<MainElement> dfs = new SaxEventInterceptor<>(MainElement.class);
    MainElement element = dfs.parse("router/sequence_label_duplicate.xml");

    assertTrue(StringUtils.startsWith(dfs.getParseErrorMsgs().get(0), "duplicate label of"));
}

}
