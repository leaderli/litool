package io.leaderli.litool.dom.parser;

import io.leaderli.litool.dom.RootBean;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * @author leaderli
 * @since 2022/7/15 8:41 AM
 */
class LiDomDFSContextTest {

    @Test
    void parse() throws IOException, ParserConfigurationException, SAXException {

        RootBean root = LiDomDFSContext.parse("bean.xml", RootBean.class);

        System.out.println(root);
    }


    @Test
    public void test() {


    }
}
