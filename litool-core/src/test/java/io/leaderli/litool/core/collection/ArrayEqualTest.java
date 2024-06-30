package io.leaderli.litool.core.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("EqualsWithItself")
class ArrayEqualTest {

    @SuppressWarnings({"RedundantArrayCreation", "ConfusingArgumentToVarargsMethod"})
    @Test
    void test() {


        Assertions.assertEquals(ArrayEqual.of(null), ArrayEqual.of(null));
        Assertions.assertEquals(ArrayEqual.of(new Object[]{}), ArrayEqual.of(new Object[]{}));
        Assertions.assertEquals(ArrayEqual.of(new Object[]{1}), ArrayEqual.of(new Object[]{1}));
        Assertions.assertNotEquals(ArrayEqual.of(new Object[]{1}), ArrayEqual.of(new Object[]{"1"}));
        Assertions.assertNotEquals(ArrayEqual.of(new Object[]{1}), ArrayEqual.of(new Object[]{1, 2}));
        Assertions.assertNotEquals(ArrayEqual.of(null), ArrayEqual.of(new Object[]{}));

        Assertions.assertTrue(ArrayEqual.of(1, 2).apply(new Integer[]{1, 2}));
    }

}
