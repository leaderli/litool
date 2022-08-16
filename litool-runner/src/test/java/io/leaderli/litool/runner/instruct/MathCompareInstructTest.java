package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.runner.constant.OperatorEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/8/16 3:07 PM
 */
class MathCompareInstructTest {

    @Test
    void test() {

        MathCompareInstruct compare = new MathCompareInstruct();

        Assertions.assertTrue((boolean) compare.apply(Boolean.class, new Object[]{OperatorEnum.EQUALS, 1, 1}));
        Assertions.assertTrue((boolean) compare.apply(Boolean.class, new Object[]{OperatorEnum.EQUALS, 1.0, 1.0}));

    }

}
