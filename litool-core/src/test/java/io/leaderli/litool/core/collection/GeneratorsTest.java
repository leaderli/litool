package io.leaderli.litool.core.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/8/31
 */
class GeneratorsTest {

    @Test
    void range() {

        Generator<Integer> range = Generators.range();

        for (int i = 0; i < 100; i++) {
            Assertions.assertEquals(i, range.next());
        }
    }
}
