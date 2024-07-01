package io.leaderli.litool.core.util;

import org.junit.jupiter.api.Assertions;
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
        Assertions.assertThrows(IllegalArgumentException.class, () -> NumberUtil.constrain(10, 5, 10, 20));
        Assertions.assertThrows(IllegalArgumentException.class, () -> NumberUtil.constrain(10.0, 5.0, 10.0, 20.0));

        double result2 = NumberUtil.constrain(10.0, 0.0, 20.0);
        assertEquals(10, result2);
        result2 = NumberUtil.constrain(10.0, 15.0, 20.0);
        assertEquals(15, result2);

        result2 = NumberUtil.constrain(-10.0, 10.0, 0.0, 20.0);
        assertEquals(10, result2);
        result2 = NumberUtil.constrain(30.0, 10, 0, 20);
        assertEquals(10, result2);
        result2 = NumberUtil.constrain(15.0, 10, 0, 20);
        assertEquals(15, result2);
    }

    @Test
    void testParserInt() {
        Assertions.assertEquals(0, NumberUtil.parserInt("0b0"));
        Assertions.assertEquals(9, NumberUtil.parserInt("011"));
        Assertions.assertEquals(17, NumberUtil.parserInt("0x11"));
        Assertions.assertEquals(15, NumberUtil.parserInt("0xF"));
    }
}
