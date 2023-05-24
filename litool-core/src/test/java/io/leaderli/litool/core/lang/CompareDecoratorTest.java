package io.leaderli.litool.core.lang;

import io.leaderli.litool.core.meta.Lira;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/9/5
 */
class CompareDecoratorTest {


    @Test
    void set() {


        List<Integer> list;

        list = Lira.range().limit(100).get();

        EqualComparator<Integer> integerEqualComparator = (a, b) -> (a / 2) - (b / 2) == 0;

        Function<Integer, CompareDecorator<Integer>> compareDecorator = i -> {
            return new CompareDecorator<>(i, integerEqualComparator);
        };
        Assertions.assertEquals(50, (int) list.stream().map(compareDecorator)
                .distinct()
                .map(m -> m.value).count());

        Assertions.assertEquals(100, list.stream().distinct().count());

        Assertions.assertEquals(50, Lira.range().limit(100).distinct(integerEqualComparator).size());


    }

}
