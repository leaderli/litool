package io.leaderli.litool.dom.sax;

import io.leaderli.litool.dom.RootBean;
import io.leaderli.litool.dom.parser.SaxEventInterceptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/8/17
 */
class IgnoreSaxBeanWithMsgTest {

    @Test
    void test() {
        SaxEventInterceptor<RootBean> dfs;

        IgnoreSaxBeanWithMsg ignoreSaxBean = new IgnoreSaxBeanWithMsg();
        dfs = new SaxEventInterceptor<>(RootBean.class, ignoreSaxBean);
        dfs.parse("bean.xml");

        Assertions.assertTrue(ignoreSaxBean.msgs.toString().contains("<yyyy>"));

    }
}
