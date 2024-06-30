package io.leaderli.litool.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DebugUtilTest {

    @Test
    void test() {
        Assertions.assertFalse(DebugUtil.isDebugMode());
    }

}
