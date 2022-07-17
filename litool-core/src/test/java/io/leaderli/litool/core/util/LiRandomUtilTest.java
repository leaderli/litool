package io.leaderli.litool.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/6/25
 */
class LiRandomUtilTest {


    @Test
    public void nextInt() {

        Assertions.assertSame(0, LiRandomUtil.nextInt(1));
        Assertions.assertTrue(LiRandomUtil.nextInt() >= 0);
        Assertions.assertSame(1, LiRandomUtil.nextInt(1, 1));
        Assertions.assertSame(1, LiRandomUtil.nextInt(1, 2));
    }
}
