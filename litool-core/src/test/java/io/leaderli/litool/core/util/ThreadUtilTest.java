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
        Assertions.assertDoesNotThrow(() -> ThreadUtil.printStack(0));
        Assertions.assertDoesNotThrow(() -> ThreadUtil.printStack(-100));
        Assertions.assertTrue(ThreadUtil.stackTrace(1).startsWith("io.leaderli.litool.core.util.ThreadUtilTest.printStack(ThreadUtilTest.java:16)"));
        Assertions.assertDoesNotThrow(() -> ThreadUtil.stackTrace(-100));
    }
}
