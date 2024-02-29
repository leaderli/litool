package io.leaderli.litool.test;

import org.junit.jupiter.api.Assertions;

public class LiTestTest {


    @LiTest
    void testParameter(int a, Bean bean) {
        Assertions.assertEquals(0, a);
        Assertions.assertNotNull(bean);
        Assertions.assertSame(bean, bean.bean);
    }

    static class Bean {

        Bean bean;

    }
}
