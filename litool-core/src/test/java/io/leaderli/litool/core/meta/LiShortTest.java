package io.leaderli.litool.core.meta;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/7/26
 */
class LiShortTest {

    @Test
    void or() {

        Assertions.assertEquals(3, LiShort.of("123").or(String::length).or(s -> 10).get());
        Assertions.assertNull(LiShort.of((String) null).or(String::length).or(s -> 10).get());
        Assertions.assertEquals(3, LiShort.of((String) null).or(String::length).or(s -> 10).def(3).get());
    }
}
