package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.runner.InstructContainer;
import io.leaderli.litool.runner.constant.DateUnitEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DateGetInstructTest {

    @Test
    public void date_get() {
        Instruct date_get = InstructContainer.getInnerMethodByAlias("date_get");
        assertEquals(date_get.apply(Integer.class, new Object[]{"20220101", DateUnitEnum.YEAR}), 2022);
    }

}