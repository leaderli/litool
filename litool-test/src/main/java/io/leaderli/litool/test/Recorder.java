package io.leaderli.litool.test;

import java.util.ArrayList;

public class Recorder extends AbstractRecorder<Recorder> {

    public Recorder(Class<?> mockClass) {
        super(mockClass);
    }

    public Recorder when(Object object) {
        return this;
    }

    public Recorder when(Runnable runnable) {
        runnable.run();
        return this;
    }

    public void build() {
        build = true;
        LiMock.record(mockClass, m -> !methodAsserts.getOrDefault(m, new ArrayList<>()).isEmpty(),
                (m, _this, args, _return) -> methodAsserts.get(m).forEach(methodAssert -> methodAssert.apply(m, _this, args, _return)));
    }


}
