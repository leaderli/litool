package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.runner.InstructContainer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BetweenTimeInstructTest {

@Test
void between_time() {
    Instruct instruct = InstructContainer.getInnerMethodByAlias("between_time");
    Object apply1 = instruct.apply(Boolean.class, new Object[]{"0700", "2000", "1200"});
    Object apply2 = instruct.apply(Boolean.class, new Object[]{"0700", "2000", "2200"});
    Object apply3 = instruct.apply(Boolean.class, new Object[]{"2000", "0700", "0500"});
    Object apply4 = instruct.apply(Boolean.class, new Object[]{"2000", "0700", "1200"});
    assertTrue((Boolean) apply1);
    assertFalse((Boolean) apply2);
    assertTrue((Boolean) apply3);
    assertFalse((Boolean) apply4);
}

}
