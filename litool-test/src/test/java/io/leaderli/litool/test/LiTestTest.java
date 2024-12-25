package io.leaderli.litool.test;

import io.leaderli.litool.test.bean.BeanCreator;
import org.junit.jupiter.api.Assertions;

@LiTestInstance
public class LiTestTest implements MockBeanBuilderConfig {


    @Mock
    Bean bean;
    @Mock
    Bean2 bean2;

    @Override
    public void init(BeanCreator.MockBeanBuilder<Void> mockBeanBuilder) {
        mockBeanBuilder.cache(LiTestTest.Bean2.class, new LiTestTest.Bean2());
    }


    @LiTest
    void testParameter(int a, Bean bean, Bean2 bean2) {
        Assertions.assertEquals(0, a);
        Assertions.assertNotNull(bean);
        Assertions.assertNotNull(this.bean);
        Assertions.assertSame(bean, bean.bean);
        Assertions.assertSame(this.bean, this.bean.bean);
        System.out.println(bean2);
        System.out.println(this.bean2);

        Assertions.assertSame(bean2, this.bean2);
    }


    static class Bean {

        Bean bean;

    }

    static class Bean2 {

        Bean2 bean;

    }
}
