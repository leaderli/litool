package io.leaderli.litool.core.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/8/30
 */
class GeneratorTest {

    @Test
    void test() {
        Generator<Integer> stream = new Generator<Integer>() {


            int i = 0;


            @Override
            public Integer next() {
                return i++;
            }
        };


        for (int i = 0; i < 100; i++) {
            stream.next();
        }
        Assertions.assertTrue(stream.hasNext());

    }

}
