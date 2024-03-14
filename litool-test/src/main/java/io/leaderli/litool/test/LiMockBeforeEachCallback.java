package io.leaderli.litool.test;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class LiMockBeforeEachCallback implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext context) {
        LiMock.clearMethodCalled();
        LiMock.reset();
    }

}
