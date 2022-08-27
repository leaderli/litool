package io.leaderli.litool.runner.xml;

import io.leaderli.litool.core.meta.LiConstant;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/8/8
 */
class EntryElementTest {

@Test
void type_error() {
    LiConstant.WHEN_THROW = null;
    SaxEventInterceptor<RequestElement> dfs = new SaxEventInterceptor<>(RequestElement.class);

    RequestElement requestElement = dfs.parse("entry.xml");


    Assertions.assertTrue(StringUtils.startsWith(dfs.getParseErrorMsgs().get(0), "the entry type  fuck is unsupported" +
            "  "));
    Assertions.assertTrue(StringUtils.startsWith(dfs.getParseErrorMsgs().get(1), "the def value a cannot satisfied " +
            "the entry type int "));
    Assertions.assertTrue(StringUtils.startsWith(dfs.getParseErrorMsgs().get(2), "the def value a cannot satisfied " +
            "the entry type int "));
    Assertions.assertTrue(StringUtils.startsWith(dfs.getParseErrorMsgs().get(3), "the entry key $ID is not match " +
            "[a-zA-Z0-9_]+ "));
}

}
