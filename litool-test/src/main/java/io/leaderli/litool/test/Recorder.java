package io.leaderli.litool.test;

import io.leaderli.litool.core.type.MethodFilter;

import java.util.ArrayList;

public class Recorder extends AbstractRecorder<Recorder> {

    public Recorder(Class<?> mockClass) {
        super(mockClass);
    }

    public Recorder when(Object object) {
        return this;
    }

    public Recorder run(Runnable runnable) {
        runnable.run();
        return this;
    }

    public void build() {
        build = true;

        LiMock.record(mockClass, MethodFilter.of(methodAsserts::containsKey),
                (m, args, _return) ->
                        methodAsserts
                                .getOrDefault(m, new ArrayList<>())
                                .forEach(methodAssert -> methodAssert.apply(m, args, _return)));
    }


}
