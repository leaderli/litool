package io.leaderli.litool.test;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class LiMockAfterEachCallback implements AfterEachCallback {
    @Override
    public void afterEach(ExtensionContext context) {
        try {

            LiMock.assertMethodCalled();
        } finally {
            LiMock.clearMethodCalled();
            LiMock.reset();
        }
    }
}
