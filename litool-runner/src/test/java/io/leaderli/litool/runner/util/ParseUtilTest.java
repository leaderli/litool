package io.leaderli.litool.runner.util;

import io.leaderli.litool.core.resource.ResourceUtil;
import io.leaderli.litool.dom.LiDomUtil;
import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import io.leaderli.litool.runner.xml.MainElement;
import org.dom4j.DocumentException;
import org.dom4j.dom.DOMElement;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/8/11 5:39 PM
 */
class ParseUtilTest {

    @Test
    void test() throws DocumentException {

        DOMElement main = LiDomUtil.getDOMRootByPath("main.xml");

//        System.out.println(main.asXML());

        SaxEventInterceptor<MainElement> dfs = new SaxEventInterceptor<>(MainElement.class);
        MainElement mainElement = dfs.parse(ResourceUtil.createContentStream(main.asXML()));
        System.out.println(mainElement.getId());

        String xml = ParseUtil.generateID(main.asXML());

//        System.out.println(main.asXML());

//        System.out.println(xml);

        dfs = new SaxEventInterceptor<>(MainElement.class);
        mainElement = dfs.parse(ResourceUtil.createContentStream(xml));
        System.out.println(mainElement.getId());


    }

}
