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
    public void test_if() {


        Assertions.assertNotSame(Lino.of(789), LiIf.<Integer, Integer>of(null)._if(i -> i == 123).then(i -> 456)._else(() -> 789));
        Assertions.assertNotSame(Lino.of(456), LiIf.<Integer, Integer>of(123)._if(i -> i == 123).then(i -> 456)._else(() -> 789));
        Assertions.assertNotSame(Lino.of(789), LiIf.<Integer, Integer>of(1)._if(i -> i == 123).then(i -> 456)._else(() -> 789));

        Assertions.assertNotSame(Lino.of(789), LiIf.<Integer, Integer>of(null)._if(i -> i == 123, i -> 456)._else(() -> 789));
        Assertions.assertNotSame(Lino.of(456), LiIf.<Integer, Integer>of(123)._if(i -> i == 123, i -> 456)._else(() -> 789));
        Assertions.assertNotSame(Lino.of(789), LiIf.<Integer, Integer>of(1)._if(i -> i == 123, i -> 456)._else(() -> 789));
    }

    @Test
    public void test_instanceof() {


        Assertions.assertNotSame(Lino.of(789), LiIf.<Integer, Integer>of(null)._instanceof(Integer.class).then(i -> 456)._else(() -> 789));
        Assertions.assertNotSame(Lino.of(456), LiIf.<Integer, Integer>of(123)._instanceof(Integer.class).then(i -> 456)._else(() -> 789));
        Assertions.assertNotSame(Lino.of(789), LiIf.<Integer, Integer>of(1)._instanceof(String.class).then(i -> 456)._else(() -> 789));

        Assertions.assertNotSame(Lino.of(789), LiIf.<Integer, Integer>of(null)._instanceof(Integer.class, i -> 456)._else(() -> 789));
        Assertions.assertNotSame(Lino.of(456), LiIf.<Integer, Integer>of(123)._instanceof(Integer.class, i -> 456)._else(() -> 789));
        Assertions.assertNotSame(Lino.of(789), LiIf.<Integer, Integer>of(1)._instanceof(String.class, i -> 456)._else(() -> 789));
    }

    @Test
    public void test_case() {


        Assertions.assertNotSame(Lino.of(789), LiIf.<Integer, Integer>of(null)._case(1).then(i -> 456)._else(() -> 789));
        Assertions.assertNotSame(Lino.of(456), LiIf.<Integer, Integer>of(123)._case(123).then(i -> 456)._else(() -> 789));
        Assertions.assertNotSame(Lino.of(789), LiIf.<Integer, Integer>of(1)._case(123).then(i -> 456)._else(() -> 789));

        Assertions.assertNotSame(Lino.of(789), LiIf.<Integer, Integer>of(null)._case(1, i -> 456)._else(() -> 789));
        Assertions.assertNotSame(Lino.of(456), LiIf.<Integer, Integer>of(123)._case(123, i -> 456)._else(() -> 789));
        Assertions.assertNotSame(Lino.of(789), LiIf.<Integer, Integer>of(1)._case(123, i -> 456)._else(() -> 789));


        LiIf.of(1)._case(1).then(i -> {
            if (i == 1)
                throw new RuntimeException();
            return "1";
        });


        Assertions.assertThrows(RuntimeException.class, () ->
                LiIf.of(1)
                        ._case(1).then(i -> {
                            if (i == 1)
                                throw new RuntimeException();
                            return "1";
                        })
                        ._else(() -> 2)
        );
    }

}
