package io.leaderli.litool.test;

import io.leaderli.litool.core.type.MethodFilter;

public class MockBean<T, R> extends AbstractMocker<T, R> {

    MockBean(Class<T> mockClass, boolean detach) {
        super(mockClass, detach);
    }


    public T build() {
        build = true;
        LiMock.mock(mockClass, MethodFilter.of(methodValueMap::containsKey), this::getMethodValue, detach);
        return instance;
    }
}
