package io.leaderli.litool.runner.xml;

import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import io.leaderli.litool.runner.Context;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

/**
 * @author leaderli
 * @since 2022/7/24
 */
class ResponseElementTest {


    @Test
    void test() {

        SaxEventInterceptor<ResponseElement> dfs = new SaxEventInterceptor<>(ResponseElement.class);
        ResponseElement responseElement = dfs.parse("response.xml");

        Context context = new Context(new HashMap<>());
        context.visit(responseElement);


        Assertions.assertEquals("", context.getResponse("CHANNEL"));
        Assertions.assertEquals(1, (int) context.getResponse("ID"));


    }
}
