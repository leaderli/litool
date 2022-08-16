package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.core.text.StringUtils;
import io.leaderli.litool.runner.InstructContainer;
import io.leaderli.litool.runner.constant.DateUnitEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatePlusInstructTest {

    @Test
    public void date_plus() {
        Instruct instruct = InstructContainer.getInnerMethodByAlias("date_plus");
        Object apply = instruct.apply(new Object[]{"20220101", DateUnitEnum.YEAR, 20});
        assertEquals(apply, "20420101");
    }

    @Test
    public void date_plus_error() {
        Instruct instruct = InstructContainer.getInnerMethodByAlias("date_plus");
        assertThrows(IllegalArgumentException.class, () -> instruct.apply(new Object[]{"202201011", DateUnitEnum.YEAR, 20}));
    }

}