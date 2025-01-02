package io.leaderli.litool.test;

import io.leaderli.litool.test.bean.BeanCreator;
import org.junit.jupiter.api.Assertions;

@LiTestInstance
public class LiTestTest implements MockBeanBuilderConfig {


    @Mock
    Bean bean;
    @Mock
    Bean2 bean2;

    BeanCreator.MockBeanBuilder<Void> beanBuilder;

    @Override
    public void init(BeanCreator.MockBeanBuilder<Void> beanBuilder) {
        beanBuilder.cache(LiTestTest.Bean2.class, new LiTestTest.Bean2());
        Assertions.assertTrue(this.beanBuilder == null || this.beanBuilder != beanBuilder);
        this.beanBuilder = beanBuilder;
    }


    @LiTest
    void testParameter(int a, Bean bean, Bean2 bean2) {
        Assertions.assertEquals(0, a);
        Assertions.assertNotNull(bean);
        Assertions.assertNotNull(this.bean);
        Assertions.assertSame(bean, bean.bean);
        Assertions.assertSame(this.bean, this.bean.bean);
        Assertions.assertNotSame(bean2, this.bean2);
    }

    @LiTest
    void testParameter2() {
        Assertions.assertNotNull(this.beanBuilder);
    }

    static class Bean {

        Bean bean;

    }

    static class Bean2 {

        Bean2 bean;

    }
}
