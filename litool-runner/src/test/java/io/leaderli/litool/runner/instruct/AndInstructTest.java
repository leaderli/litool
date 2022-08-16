package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.runner.InstructContainer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AndInstructTest {

    @Test
    public void and() {
        Instruct instruct = InstructContainer.getInnerMethodByAlias("and");
        Object apply = instruct.apply(new Object[]{true, false});
        assertFalse((Boolean) apply);

        apply = instruct.apply(new Object[]{true, true});
        assertTrue((Boolean) apply);
    }

}