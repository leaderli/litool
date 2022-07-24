package io.leaderli.litool.dom.parser;

import io.leaderli.litool.dom.RootBean;
import io.leaderli.litool.dom.sax.IgnoreSaxBeanWithMsg;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/7/15 8:41 AM
 */
class SaxEventInterceptorTest {

    @Test
    void parse() {

        SaxEventInterceptor<RootBean> dfs = new SaxEventInterceptor<>(RootBean.class);
        RootBean root = dfs.parse("bean.xml");
        Assertions.assertEquals("no", root.nobean.name);
        Map<String, String> map = new HashMap<>();
        map.put("123", "abc");
        Assertions.assertEquals("abc", root.nobean.body.get(map));


        IgnoreSaxBeanWithMsg ignoreSaxBean = new IgnoreSaxBeanWithMsg();
        dfs = new SaxEventInterceptor<>(RootBean.class, ignoreSaxBean);
        dfs.parse("bean.xml");

        Assertions.assertTrue(ignoreSaxBean.msgs.toString().contains("<yyyy>"));


    }


}
