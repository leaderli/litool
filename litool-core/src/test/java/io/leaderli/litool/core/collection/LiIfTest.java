package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.condition.LiIf;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/6/24
 */
class LiIfTest {

    @Test
    public void test() {

        System.out.println(LiIf.of(123)._if(i -> i == 123).then(i -> 789)._else(() -> 456));
    }
}
