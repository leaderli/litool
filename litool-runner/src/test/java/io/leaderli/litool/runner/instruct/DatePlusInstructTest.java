package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.runner.InstructContainer;
import io.leaderli.litool.runner.constant.DateUnitEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DatePlusInstructTest {

@Test
void date_plus() {
    Instruct instruct = InstructContainer.getInnerMethodByAlias("date_plus");
    Object apply = instruct.apply(String.class, new Object[]{"20220101", DateUnitEnum.YEAR, 20});
    assertEquals("20420101", apply);
}

@Test
void date_plus_error() {
    Instruct instruct = InstructContainer.getInnerMethodByAlias("date_plus");
    assertThrows(IllegalArgumentException.class, () -> instruct.apply(String.class, new Object[]{"202201011",
            DateUnitEnum.YEAR, 20}));
}

}
