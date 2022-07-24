package io.leaderli.litool.runner.xml;

import io.leaderli.litool.core.meta.LiConstant;
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
class ResponseElementTest {


    @Test
    void test() {
        SaxEventInterceptor<ResponseElement> dfs = new SaxEventInterceptor<>(ResponseElement.class);

        ResponseElement responseElement = dfs.parse("response.xml");

        Map<String, Object> response = new HashMap<>();


        LiConstant.WHEN_THROW = null;
        responseElement.entryList.lira().forEach(entry -> {

            String key = entry.key.text;
            Class<?> type = TypeAlias.getType(entry.type);
            Object parserValue = TypeAlias.parser(entry.type, null, entry.def);

            response.put(key, parserValue);
        });


        Assertions.assertEquals("", response.get("CHANNEL"));
        Assertions.assertEquals(1, response.get("ID"));


    }
}
