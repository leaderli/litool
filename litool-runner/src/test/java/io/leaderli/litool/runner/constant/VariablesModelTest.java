package io.leaderli.litool.runner.constant;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/8/10
 */
class VariablesModelTest {


@Test
void test() {

    Assertions.assertThrows(UnsupportedOperationException.class, () -> VariablesModel.ERROR.apply(null, null));

}

}
