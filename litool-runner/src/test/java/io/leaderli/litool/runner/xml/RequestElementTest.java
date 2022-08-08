package io.leaderli.litool.runner.xml;

import io.leaderli.litool.core.collection.ImmutableMap;
import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import io.leaderli.litool.runner.TypeAlias;
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
        Map<String, Object> parserRequest = new HashMap<>();
        request.put("CHANNEL", "IVR");


        requestElement.entryList.lira().forEach(entry -> {

            String text = entry.getKey();
            String value = request.getOrDefault(text, entry.getDef());
            Class<?> type = TypeAlias.getType(entry.getType());
            Object parserValue = TypeAlias.parser(entry.getType(), value, entry.getDef());

            parserRequest.put(text, parserValue);
        });

        ImmutableMap<String, Object> of = ImmutableMap.of(parserRequest);


        Assertions.assertEquals("IVR", of.get("CHANNEL"));
        Assertions.assertEquals(1, of.get("ID"));


    }

    @Test
    void error() {
        SaxEventInterceptor<RequestElement> dfs = new SaxEventInterceptor<>(RequestElement.class);

        RequestElement requestElement = dfs.parse("request_error.xml");

        System.out.println(dfs.getParseErrorMsgs());

        Assertions.assertEquals("duplicate key of CHANNEL at line:4 column:23", dfs.getParseErrorMsgs().get(0));
    }
}
