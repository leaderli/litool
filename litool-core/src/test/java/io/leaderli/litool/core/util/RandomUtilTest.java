package io.leaderli.litool.core.util;

import io.leaderli.litool.core.meta.LiBox;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/6/25
 */
class RandomUtilTest {


    @Test
    void shunt() {

        Assertions.assertTrue(RandomUtil.shunt(1));

        boolean flag = true;
        for (int i = 0; i < 10; i++) {
            flag = RandomUtil.shunt(100);
            if (!flag) {
                break;
            }
        }
        Assertions.assertFalse(flag);
    }

    @Test
    void nextInt() {

        Assertions.assertSame(0, RandomUtil.nextInt(1));
        Assertions.assertTrue(RandomUtil.nextInt() >= 0);
        Assertions.assertSame(1, RandomUtil.nextInt(1, 1));
        Assertions.assertSame(1, RandomUtil.nextInt(1, 2));
    }

    @Test
    void randomRun() {
        LiBox<Object> none = LiBox.none();
        for (int i = 0; i < 50; i++) {
            RandomUtil.randomRun(2, () -> none.value(1));
        }
        Assertions.assertTrue(none.present());
    }
}
