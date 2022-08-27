package io.leaderli.litool.core.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/1/22
 */
class LiAssertUtilTest {


@SuppressWarnings("all")
@Test
void test() {

    Assertions.assertThrows(AssertException.class, () -> {
        LiAssertUtil.assertTrue(false);
    });

    AssertException thrown = Assertions.assertThrows(AssertException.class, () -> {
        LiAssertUtil.assertTrue(false, "123");
    });
    Assertions.assertEquals(thrown.getMessage(), "123");

    Assertions.assertThrows(AssertException.class, () -> {
        LiAssertUtil.assertFalse(true);
    });

    thrown = Assertions.assertThrows(AssertException.class, () -> {
        LiAssertUtil.assertFalse(true, "123");
    });
    Assertions.assertEquals(thrown.getMessage(), "123");
}
}
