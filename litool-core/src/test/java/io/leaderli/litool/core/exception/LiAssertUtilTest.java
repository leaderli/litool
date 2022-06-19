package io.leaderli.litool.core.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/1/22
 */
public class LiAssertUtilTest {


    @SuppressWarnings("all")
    @Test
    public void test() {

        Assertions.assertThrows(LiAssertException.class, () -> {
            LiAssertUtil.assertTrue(false);
        });

        LiAssertException thrown = Assertions.assertThrows(LiAssertException.class, () -> {
            LiAssertUtil.assertTrue(false, "123");
        });
        Assertions.assertEquals(thrown.getMessage(), "123");

        Assertions.assertThrows(LiAssertException.class, () -> {
            LiAssertUtil.assertFalse(true);
        });

        thrown = Assertions.assertThrows(LiAssertException.class, () -> {
            LiAssertUtil.assertFalse(true, "123");
        });
        Assertions.assertEquals(thrown.getMessage(), "123");
    }
}
