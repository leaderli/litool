package io.leaderli.litool.test;

import io.leaderli.litool.core.type.MethodFilter;

import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings({"rawtypes", "unchecked"})
public class Mocker extends BaseMockerForBean<Void, Object> {

    Mocker(Class mockClass, boolean detach) {
        super(mockClass, detach);
    }


    public Void build() {
        build = true;
        LiMock.mock(mockClass, MethodFilter.of(methodValueMap::containsKey), this::getMethodValue, detach);
        return null;
    }

    @Override
    public IMocker consume(Consumer call) {
        throw new UnsupportedOperationException("mocker don't have instance");
    }

    public <R> IMocker function(Function<Void, R> call) {
        throw new UnsupportedOperationException("mocker don't have instance");
    }
}
