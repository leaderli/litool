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

        Assertions.assertTrue(OperatorEnum.EQUALS.apply(1, 1));
        Assertions.assertTrue(OperatorEnum.GREATER_THAN.apply(2, 1));
        Assertions.assertTrue(OperatorEnum.GREATER_THAN_OR_EQUALS.apply(1, 1));
        Assertions.assertTrue(OperatorEnum.LESS_THAN.apply(1, 2));
        Assertions.assertTrue(OperatorEnum.LESS_THAN_OR_EQUALS.apply(1, 1));

        Assertions.assertFalse(OperatorEnum.EQUALS.apply(1, 2));
        Assertions.assertFalse(OperatorEnum.GREATER_THAN.apply(1, 2));
        Assertions.assertFalse(OperatorEnum.GREATER_THAN_OR_EQUALS.apply(1, 2));
        Assertions.assertFalse(OperatorEnum.LESS_THAN.apply(1, 1));
        Assertions.assertFalse(OperatorEnum.LESS_THAN_OR_EQUALS.apply(2, 1));

    }

}
