package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.runner.InstructContainer;
import io.leaderli.litool.runner.constant.OperatorEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DateCompareInstructTest {

    @Test
    void date_compare() {
        Instruct instruct = InstructContainer.getInnerMethodByAlias("date_compare");
        Object apply = instruct.apply(Boolean.class, new Object[]{OperatorEnum.GREATER_THAN, "20220102", "20220101"});

        assertTrue((Boolean) apply);
    }

}
