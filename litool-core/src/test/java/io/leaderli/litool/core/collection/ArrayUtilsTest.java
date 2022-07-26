package io.leaderli.litool.core.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author leaderli
 * @since 2022/7/26 9:09 AM
 */
class ArrayUtilsTest {

    @Test
    void union() {
        int[] a = new int[]{1};
        int [] b = new int[]{2};
        Assertions.assertEquals("[1, 2]", Arrays.toString(ArrayUtils.union(a, b)));
    }
}
