package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.runner.InstructContainer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AndInstructTest {

    @Test
    void and() {
        Instruct instruct = InstructContainer.getInnerMethodByAlias("and");
        Object apply = instruct.apply(Boolean.class, new Object[]{true, false});
        assertFalse((Boolean) apply);

        apply = instruct.apply(Boolean.class, new Object[]{true, true});
        assertTrue((Boolean) apply);
    }

}
