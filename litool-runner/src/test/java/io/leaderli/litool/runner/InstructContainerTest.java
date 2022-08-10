package io.leaderli.litool.runner;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/8/9
 */
class InstructContainerTest {


    @Test
    void scan() {

        Map<String, Method> scanner = InnerFuncContainer.scanner();
        Assertions.assertTrue(scanner.size() > 0);
    }

}
