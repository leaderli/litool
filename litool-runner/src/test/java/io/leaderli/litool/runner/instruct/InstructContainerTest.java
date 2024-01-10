package io.leaderli.litool.runner.instruct;

import io.leaderli.litool.core.exception.AssertException;
import io.leaderli.litool.runner.InstructContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author leaderli
 * @since 2022/8/9
 */
class InstructContainerTest {


    @Test
    void scan() {

        Map<String, Instruct> scanner = InstructContainer.scanner();
        Assertions.assertFalse(scanner.isEmpty());

        Assertions.assertThrows(AssertException.class, () -> InstructContainer.registerInstruct(new AddInstruct()));

    }

}
