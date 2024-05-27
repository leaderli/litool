package io.leaderli.litool.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author leaderli
 * @since 2023/6/21 12:43 PM
 */
class NumberUtilTest {

    @Test
    void constrain() {
        int result = NumberUtil.constrain(10, 0, 20);
        assertEquals(10, result);
        result = NumberUtil.constrain(-10, 0, 20);
        assertEquals(0, result);
        result = NumberUtil.constrain(30, 0, 20);
        assertEquals(20, result);
        result = NumberUtil.constrain(10, 5, 0, 20);
        assertEquals(10, result);
        result = NumberUtil.constrain(-10, 10, 0, 20);
        assertEquals(10, result);
        result = NumberUtil.constrain(30, 10, 0, 20);
        assertEquals(10, result);
    }
}
