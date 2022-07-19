package io.leaderli.litool.dom.parser;

import io.leaderli.litool.core.type.ClassLoaderUtil;
import io.leaderli.litool.dom.LiDomParser;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/7/17
 */
class LiDomParserFactoryTest {

    @Test
    void create() {


//        Assertions.assertEquals(Lino.none(), LiDomParserFactory.create("root"));
//        LiDomParserFactory.register("root", RootBean.class);
//        Assertions.assertEquals(RootBean.class, LiDomParserFactory.create("root").get().getClass());

        String name = LiDomParser.class.getPackage().getName();
        System.out.println(name);
        System.out.println(ClassLoaderUtil.getClassLoader().getResource(name));
        name = "io";

        System.out.println(ClassLoaderUtil.getClassLoader().getResource(name));


    }
}
