package io.leaderli.litool.dom.parser;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.core.type.ClassUtil;
import io.leaderli.litool.core.type.MethodScanner;
import io.leaderli.litool.dom.RootBean;
import io.leaderli.litool.dom.sax.IgnoreSaxBeanWithMsg;
import io.leaderli.litool.dom.sax.SaxBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/7/15 8:41 AM
 */
class SaxEventInterceptorTest {


    @Test
    void test() {

        String methodName = "bean";
        MethodScanner methodScanner = MethodScanner.of(RootBean.class, false, method ->

                StringUtils.equalsAnyIgnoreCase(method.getName(), "set" + methodName, "add" + methodName)
                        && method.getParameterCount() == 1
                        && ClassUtil.isAssignableFromOrIsWrapper(SaxBean.class, method.getParameterTypes()[0]));

        Lino<Method> first = methodScanner.scan()
                .sort((m1, m2) -> m2.getName().compareTo(m1.getName()))
                .first();

        Assertions.assertEquals("addBean", first.get().getName());


    }

    @Test
    void parse() {

        Map<String, String> map = new HashMap<>();
        map.put("123", "abc");

        SaxEventInterceptor<RootBean> dfs = new SaxEventInterceptor<>(RootBean.class);
        RootBean root = dfs.parse("bean.xml");

        Assertions.assertTrue(root.beans.lira().first().present());
        Assertions.assertEquals("no", root.getNoBean().getName());
        Assertions.assertEquals("abc", map.get(root.getNoBean().body));


        IgnoreSaxBeanWithMsg ignoreSaxBean = new IgnoreSaxBeanWithMsg();
        dfs = new SaxEventInterceptor<>(RootBean.class, ignoreSaxBean);
        dfs.parse("bean.xml");

        Assertions.assertTrue(ignoreSaxBean.msgs.toString().contains("<yyyy>"));

    }

    @Test
    void complex() {
        SaxEventInterceptor<Sax> dfs = new SaxEventInterceptor<>(Sax.class);
        Sax sax = dfs.parse("sax_complex_field.xml");

        Assertions.assertSame(Color.RED, sax.color);
    }


}
