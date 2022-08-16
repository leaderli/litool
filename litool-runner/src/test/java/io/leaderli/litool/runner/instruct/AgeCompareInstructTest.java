package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.runner.InstructContainer;
import io.leaderli.litool.runner.constant.OperatorEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AgeCompareInstructTest {

    @Test
    public void age_compare() {
        Instruct age_compare = InstructContainer.getInnerMethodByAlias("age_compare");
        Object apply = age_compare.apply(new Object[]{OperatorEnum.GREATER_THAN, "19900101", "20"});
        assertTrue((Boolean) apply);

        apply = age_compare.apply(new Object[]{OperatorEnum.GREATER_THAN, "19900101", "40"});
        assertFalse((Boolean) apply);
    }

}