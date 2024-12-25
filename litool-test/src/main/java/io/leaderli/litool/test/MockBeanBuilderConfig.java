package io.leaderli.litool.test;

import io.leaderli.litool.test.bean.BeanCreator;

public interface MockBeanBuilderConfig {

    void init(BeanCreator.MockBeanBuilder<Void> mockBeanBuilder);
}
