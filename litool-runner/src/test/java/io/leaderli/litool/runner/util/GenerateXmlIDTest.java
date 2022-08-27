package io.leaderli.litool.runner.util;

import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import io.leaderli.litool.runner.xml.MainElement;
import org.dom4j.DocumentException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

/**
 * @author leaderli
 * @since 2022/8/11 5:39 PM
 */
class GenerateXmlIDTest {

@Test
void test() throws DocumentException {

    InputStream inputStream = GenerateXmlID.generateByPath("main.xml");

    SaxEventInterceptor<MainElement> dfs = new SaxEventInterceptor<>(MainElement.class);
    MainElement main = dfs.parse(inputStream);
    Assertions.assertEquals("2", main.getRequest().entryList.lira().first().get().getId());


}

}
