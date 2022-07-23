package io.leaderli.litool.dom.parser;

import io.leaderli.litool.dom.RootBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/7/15 8:41 AM
 */
class LiDomDFSContextTest {

    @Test
    void parse() throws IOException, ParserConfigurationException, SAXException {

        RootBean root = LiDomDFSContext.parse("bean.xml", RootBean.class);

        Assertions.assertEquals("no", root.nobean.name);
        Map<String, String> map = new HashMap<>();
        map.put("123", "abc");
        Assertions.assertEquals("abc", root.nobean.body.get(map));
    }


}
