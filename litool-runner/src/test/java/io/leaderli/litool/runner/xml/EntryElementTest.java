package io.leaderli.litool.runner.xml;

import io.leaderli.litool.core.meta.LiConstant;
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


        Assertions.assertEquals("the entry type  fuck is unsupported  at line:3 column:44", dfs.getParseErrorMsgs().get(0));
        Assertions.assertEquals("the def value a cannot satisfied the entry type int at line:4 column:54", dfs.getParseErrorMsgs().get(1));
        Assertions.assertEquals("the def value a cannot satisfied the entry type int at line:5 column:54", dfs.getParseErrorMsgs().get(2));
        Assertions.assertEquals("the entry key $ID is not match [a-zA-Z0-9_]+ at line:6 column:48", dfs.getParseErrorMsgs().get(3));
    }

}
