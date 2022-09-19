package io.leaderli.litool.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/9/15 10:52 AM
 */
class ThreadUtilTest {

    @Test
    void printStack() {
        Assertions.assertDoesNotThrow(() -> ThreadUtil.printStack(1));
    }
}
