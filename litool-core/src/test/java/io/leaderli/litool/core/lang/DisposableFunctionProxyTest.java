package io.leaderli.litool.core.lang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/9/5
 */
class DisposableFunctionProxyTest {

    @Test
    void apply() {

        int count = 0;

        Function<Integer, Integer> function = i -> i + 1;
        Assertions.assertEquals(0, count);
        count = function.apply(count);
        Assertions.assertEquals(1, count);
        count = function.apply(count);
        Assertions.assertEquals(2, count);
        function = DisposableFunctionProxy.of(function);
        count = function.apply(count);
        Assertions.assertEquals(3, count);
        count = function.apply(count);
        Assertions.assertEquals(3, count);

    }
}
