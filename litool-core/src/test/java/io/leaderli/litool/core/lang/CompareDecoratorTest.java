package io.leaderli.litool.core.lang;

import io.leaderli.litool.core.meta.Lira;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author leaderli
 * @since 2022/9/5
 */
class CompareDecoratorTest {


    @Test
    void set() {


        List<Integer> list;

        list = Lira.range().limit(100).get();

        Assertions.assertEquals(50, (int) list.stream().map(i -> new CompareDecorator<>(i,
                        (a, b) -> (a / 2) - (b / 2) == 0))
                .distinct()
                .map(m -> m.value).count());


    }

}
