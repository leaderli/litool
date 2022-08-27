package io.leaderli.litool.runner.xml;

import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import io.leaderli.litool.runner.Context;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/7/24
 */
class RequestElementTest {


@Test
void test() {
    SaxEventInterceptor<RequestElement> dfs = new SaxEventInterceptor<>(RequestElement.class);

    RequestElement requestElement = dfs.parse("request.xml");

    Map<String, String> request = new HashMap<>();
    request.put("CHANNEL", "IVR");


    Context context = new Context(request);
    requestElement.executor().visit(context);
//        requestElement.visit(context);
//        context.visit(requestElement);

//        new RequestElementExecutor(requestElement).visit(context);


    Assertions.assertEquals("IVR", context.getRequest("CHANNEL"));
    Assertions.assertEquals(1, (int) context.getRequest("ID"));


}

@Test
void error() {
    SaxEventInterceptor<RequestElement> dfs = new SaxEventInterceptor<>(RequestElement.class);

    RequestElement requestElement = dfs.parse("request_error.xml");


    Assertions.assertTrue(StringUtils.startsWith(dfs.getParseErrorMsgs().get(0), "duplicate key of CHANNEL "));
    Assertions.assertTrue(StringUtils.startsWith(dfs.getParseErrorMsgs().get(1), "duplicate label of 渠道 "));
}

}
