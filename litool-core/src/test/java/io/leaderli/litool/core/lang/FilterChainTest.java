package io.leaderli.litool.core.lang;

import io.leaderli.litool.core.meta.LiBox;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FilterChainTest {

    @Test
    void test() {

        FilterChain<Integer> filter = new FilterChain<>();
        filter.add(i -> i > 10);

        Assertions.assertTrue(filter.apply(11));
        Assertions.assertFalse(filter.apply(9));
        filter.add(i -> i < 100);
        Assertions.assertTrue(filter.apply(11));
        Assertions.assertFalse(filter.apply(101));

        FilterChain<Integer> filter2 = new FilterChain<>();
        filter2.add(filter);
        Assertions.assertFalse(filter2.apply(9));
        Assertions.assertTrue(filter2.apply(11));
        Assertions.assertFalse(filter2.apply(101));

        FilterChain<Integer> filter3 = new FilterChain<>();
        filter3.add(filter2);
        filter3.add(i -> i % 2 == 0);
        Assertions.assertFalse(filter3.apply(9));
        Assertions.assertTrue(filter3.apply(12));
        Assertions.assertFalse(filter3.apply(11));
        Assertions.assertFalse(filter3.apply(101));

        FilterChain<Integer> filter4 = new FilterChain<>();
        LiBox<Integer> box = LiBox.of(0);
        filter4.add(i -> {
            box.value(1);
            return true;
        });
        filter4.add(i -> {
            box.value(2);
            return false;
        });
        filter4.apply(1);

        Assertions.assertEquals(2, box.value());
        box.value(0);
        filter4.addHead(i -> false);
        filter4.apply(1);
        Assertions.assertEquals(0, box.value());

    }

}
