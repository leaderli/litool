package io.leaderli.litool.test;

import java.lang.reflect.Method;
import java.util.function.BiFunction;

public class Mocker extends AbstractMocker<Mocker> {

    Mocker(Class<?> mockClass, boolean detach) {
        super(mockClass, detach);
    }


    public <T> Mocker when(T call, T result) {
        return record(result, null, 0b10);
    }

    public <T> Mocker run(Runnable call) {
        call.run();
        return record(null, (m, args) -> null, 0b01);

    }

    public <T> Mocker when(T call, T result, T other) {
        return record(result, (m, args) -> other, 0b11);
    }

    public <T> Mocker other(T call, BiFunction<Method, Object[], T> otherValue) {
        return record(null, otherValue, 0b01);
    }

    public <T> Mocker other(T call, T result, BiFunction<Method, Object[], T> otherValue) {
        return record(result, otherValue, 0b11);
    }

    public void build() {
        build = true;
        LiMock.mock(mockClass, methodValueMap::containsKey, this::getMethodValue, detach);
    }

}
