package io.leaderli.litool.runner.instruct;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/8/16 3:22 PM
 */
class AddInstructTest {

    @Test
    void test() {
        AddInstruct add = new AddInstruct();

        Assertions.assertEquals(1, (int) add.apply(Integer.class, new Object[]{2, 1}));
        Assertions.assertEquals(0.0, (double) add.apply(Double.class, new Object[]{1.0, 1.0}));


    }

}
