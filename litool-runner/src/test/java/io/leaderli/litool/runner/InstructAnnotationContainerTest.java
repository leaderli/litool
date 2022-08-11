package io.leaderli.litool.runner;

import io.leaderli.litool.runner.instruct.Instruct;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author leaderli
 * @since 2022/8/9
 */
class InstructAnnotationContainerTest {


    @Test
    void scan() {

        Map<String, Instruct> scanner = InstructContainer.scanner();
        Assertions.assertTrue(scanner.size() > 0);
    }

}
