package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.runner.InstructContainer;
import io.leaderli.litool.runner.constant.OperatorEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AgeCompareInstructTest {

@Test
void age_compare() {
    Instruct age_compare = InstructContainer.getInnerMethodByAlias("age_compare");
    Object apply = age_compare.apply(Boolean.class, new Object[]{OperatorEnum.GREATER_THAN, "19900101", "20"});
    assertTrue((Boolean) apply);

    apply = age_compare.apply(Boolean.class, new Object[]{OperatorEnum.GREATER_THAN, "19900101", "40"});
    assertFalse((Boolean) apply);
}

}
