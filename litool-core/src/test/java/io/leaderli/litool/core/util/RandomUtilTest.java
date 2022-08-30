package io.leaderli.litool.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/6/25
 */
class RandomUtilTest {


    @Test
    void nextInt() {

        Assertions.assertSame(0, RandomUtil.nextInt(1));
        Assertions.assertTrue(RandomUtil.nextInt() >= 0);
        Assertions.assertSame(1, RandomUtil.nextInt(1, 1));
        Assertions.assertSame(1, RandomUtil.nextInt(1, 2));
    }
}
