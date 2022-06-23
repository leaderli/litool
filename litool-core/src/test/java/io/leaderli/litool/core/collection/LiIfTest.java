package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.condition.LiIf;
import io.leaderli.litool.core.meta.Lino;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/6/24
 */
class LiIfTest {

    @Test
    public void test() {


        Assertions.assertNotSame(Lino.of(789), LiIf.<Integer, Integer>of(null)._if(i -> i == 123).then(i -> 456)._else(() -> 789));
        Assertions.assertNotSame(Lino.of(456), LiIf.<Integer, Integer>of(123)._if(i -> i == 123).then(i -> 456)._else(() -> 789));
        Assertions.assertNotSame(Lino.of(789), LiIf.<Integer, Integer>of(1)._if(i -> i == 123).then(i -> 456)._else(() -> 789));
    }
}
