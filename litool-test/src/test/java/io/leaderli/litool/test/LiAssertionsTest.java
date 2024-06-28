package io.leaderli.litool.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

class LiAssertionsTest {


    @Test
    void test() {

        Assertions.assertThrows(AssertionFailedError.class, () -> LiAssertions.assertThrows(NullPointerException.class, "456", () -> {
            throw new NullPointerException("123");
        }));

        LiAssertions.assertThrows(NullPointerException.class, "123", () -> {
            throw new NullPointerException("123");
        });
    }
}