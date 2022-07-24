package io.leaderli.litool.runner.xml;

import io.leaderli.litool.core.collection.ImmutableMap;
import io.leaderli.litool.dom.parser.LiDomDFSContext;
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
        LiDomDFSContext<RequestElement> dfs = new LiDomDFSContext<>(RequestElement.class);

        RequestElement requestElement = dfs.parse("request.xml");

        Map<String, String> request = new HashMap<>();
        Map<String, Object> parserRequest = new HashMap<>();
        request.put("CHANNEL", "IVR");


        requestElement.entryList.copy().forEach(entry -> {

            String text = entry.key.text;
            String value = request.getOrDefault(text, entry.def);
            Class<?> type = TypeAlias.getType(entry.type);
            Object parserValue = TypeAlias.parser(entry.type, value, entry.def);

            parserRequest.put(text, parserValue);
        });

        ImmutableMap<String, Object> of = ImmutableMap.of(parserRequest);


        Assertions.assertEquals("IVR", of.get("CHANNEL"));
        Assertions.assertEquals(1, of.get("ID"));


    }
}
