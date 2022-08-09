package io.leaderli.litool.core.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author leaderli
 * @since 2022/7/26 9:09 AM
 */
class ArrayUtilsTest {

    @Test
    void union() {
        int[] a = new int[]{1};
        int[] b = new int[]{2};
        Assertions.assertEquals("[1, 2]", Arrays.toString(ArrayUtils.combination(a, b)));
    }


    @Test
    void sub() {

        Object[] original = null;

        Assertions.assertNull(ArrayUtils.sub(original, 1, 2));
        original = new String[]{};
        Assertions.assertEquals(0, ArrayUtils.sub(original, 1, 2).length);
        original = new String[]{"1","2","3"};
        Assertions.assertEquals(0, ArrayUtils.sub(original, 2, 2).length);

        original = new String[]{"1","2","3"};
        Assertions.assertEquals(0, ArrayUtils.sub(original, 2, 1).length);
        original = new String[]{"1","2","3"};
        Assertions.assertEquals("3", ArrayUtils.sub(original, 2, 3)[0]);
        original = new String[]{"1","2","3"};
        Assertions.assertEquals("3", ArrayUtils.sub(original, 2, -1)[0]);
//        System.out.println(Arrays.toString(Arrays.copyOfRange(original, 1, 2)));

    }
}
