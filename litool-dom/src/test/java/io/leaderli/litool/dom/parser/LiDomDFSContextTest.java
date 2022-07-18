package io.leaderli.litool.dom.parser;

import io.leaderli.litool.dom.RootBean;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author leaderli
 * @since 2022/7/15 8:41 AM
 */
class LiDomDFSContextTest {

    @Test
    void parse() throws IOException {

        RootBean root = LiDomDFSContext.parse("bean.xml", RootBean.class);

    }


    @Test
    public void test() {


    }
}
