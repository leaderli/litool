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
    void probability() {

        Assertions.assertTrue(RandomUtil.probability(1));

        boolean flag = true;
        for (int i = 0; i < 10; i++) {
            flag = RandomUtil.probability(100);
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
        LiBox<Object> run = LiBox.none();
        for (int i = 0; i < 50; i++) {
            RandomUtil.randomRun(2, () -> run.value(1));
        }
        Assertions.assertTrue(run.present());
    }

    @Test
    void randomGet() {
        LiBox<Object> run = LiBox.none();
        for (int i = 0; i < 50; i++) {
            RandomUtil.randomGet(2, () -> 1).ifPresent(a -> run.value(true));

        }
        Assertions.assertTrue(run.present());
    }

    @Test
    void testRandom() {
        Assertions.assertNotEquals(",", RandomUtil.randomString(1));
        Assertions.assertEquals("aa", RandomUtil.randomString("a", 2));
        Assertions.assertEquals("", RandomUtil.randomString("", 2));
        Assertions.assertEquals("a", RandomUtil.randomString("a", -1));
        Assertions.assertTrue(RandomUtil.randomNumerString(5).matches("\\d{5}"));
    }
}
