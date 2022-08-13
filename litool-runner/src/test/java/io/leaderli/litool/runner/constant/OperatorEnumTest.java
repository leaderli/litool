package io.leaderli.litool.runner.constant;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/8/13 9:53 AM
 */
class OperatorEnumTest {

    @Test
    void test() {

        Assertions.assertTrue(OperatorEnum.EQUALS.compare(1, 1));
        Assertions.assertTrue(OperatorEnum.GREATER_THAN.compare(2, 1));
        Assertions.assertTrue(OperatorEnum.GREATER_THAN_OR_EQUALS.compare(1, 1));
        Assertions.assertTrue(OperatorEnum.LESS_THAN.compare(1, 2));
        Assertions.assertTrue(OperatorEnum.LESS_THAN_OR_EQUALS.compare(1, 1));

        Assertions.assertFalse(OperatorEnum.EQUALS.compare(1, 2));
        Assertions.assertFalse(OperatorEnum.GREATER_THAN.compare(1, 2));
        Assertions.assertFalse(OperatorEnum.GREATER_THAN_OR_EQUALS.compare(1, 2));
        Assertions.assertFalse(OperatorEnum.LESS_THAN.compare(1, 1));
        Assertions.assertFalse(OperatorEnum.LESS_THAN_OR_EQUALS.compare(2, 1));

    }

}
