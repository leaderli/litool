package io.leaderli.litool.runner.util;

import io.leaderli.litool.runner.xml.MainElement;
import org.dom4j.DocumentException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/8/11 5:39 PM
 */
class RunnerSaxEventInterceptorUtilTest {

    @Test
    void test() throws DocumentException {

        MainElement main = RunnerSaxEventInterceptorUtil.parse("main.xml", MainElement.class);

        Assertions.assertEquals("2", main.getRequest().entryList.lira().first().get().id());


    }

}
