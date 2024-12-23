package io.leaderli.litool.test;

import io.leaderli.litool.core.type.BeanCreator;

public interface MockBeanBuilderConfig {

    void init(BeanCreator.MockBeanBuilder<Void> mockBeanBuilder);
}
