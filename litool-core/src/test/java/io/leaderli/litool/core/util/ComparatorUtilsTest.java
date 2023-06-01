package io.leaderli.litool.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ComparatorUtilsTest {

    @Test
    void test() {

        Assertions.assertEquals(0, ComparatorUtils.compareNum(1, 1.0));
        Assertions.assertEquals(-1, ComparatorUtils.compareNum(1, 1.1));

    }
}
