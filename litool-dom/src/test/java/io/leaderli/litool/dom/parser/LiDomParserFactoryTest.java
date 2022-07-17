package io.leaderli.litool.dom.parser;

import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.dom.RootBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/7/17
 */
class LiDomParserFactoryTest {

    @Test
    void create() {


        Assertions.assertEquals(Lino.none(), LiDomParserFactory.create("root"));
        LiDomParserFactory.register("root", RootBean.class);
        Assertions.assertEquals(RootBean.class, LiDomParserFactory.create("root").get().getClass());

    }
}
