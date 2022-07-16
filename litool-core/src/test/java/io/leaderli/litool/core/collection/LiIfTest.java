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


        Assertions.assertEquals(Lino.of(789), LiIf.<Integer, Integer>of(null)._if(i -> i == 123).then(i -> 456)._else(() -> 789));
        Assertions.assertEquals(Lino.of(456), LiIf.<Integer, Integer>of(123)._if(i -> i == 123).then(i -> 456)._else(() -> 789));
        Assertions.assertEquals(Lino.of(789), LiIf.<Integer, Integer>of(1)._if(i -> i == 123).then(i -> 456)._else(() -> 789));

        Assertions.assertEquals(Lino.of(789), LiIf.<Integer, Integer>of(null)._if(i -> i == 123, i -> 456)._else(() -> 789));
        Assertions.assertEquals(Lino.of(456), LiIf.<Integer, Integer>of(123)._if(i -> i == 123, i -> 456)._else(() -> 789));
        Assertions.assertEquals(Lino.of(789), LiIf.<Integer, Integer>of(1)._if(i -> i == 123, i -> 456)._else(() -> 789));
    }

    @Test
    public void test_instanceof() {


        Assertions.assertEquals(Lino.of(789), LiIf.<Integer, Integer>of(null)._instanceof(Integer.class).then(i -> 456)._else(() -> 789));
        Assertions.assertEquals(Lino.of(456), LiIf.<Integer, Integer>of(123)._instanceof(Integer.class).then(i -> 456)._else(() -> 789));
        Assertions.assertEquals(Lino.of(789), LiIf.<Integer, Integer>of(1)._instanceof(String.class).then(i -> 456)._else(() -> 789));

        Assertions.assertEquals(Lino.of(789), LiIf.<Integer, Integer>of(null)._instanceof(Integer.class, i -> 456)._else(() -> 789));
        Assertions.assertEquals(Lino.of(456), LiIf.<Integer, Integer>of(123)._instanceof(Integer.class, i -> 456)._else(() -> 789));
        Assertions.assertEquals(Lino.of(789), LiIf.<Integer, Integer>of(1)._instanceof(String.class, i -> 456)._else(() -> 789));


        Assertions.assertEquals(Lino.of(789), LiIf.<Integer, Integer>of(1)._instanceof((Class<?>) null).then(i -> 456)._else(() -> 789));

    }

    @Test
    public void test_case() {


        Assertions.assertEquals(Lino.of(789), LiIf.<Integer, Integer>of(null)._case(1).then(i -> 456)._else(() -> 789));
        Assertions.assertEquals(Lino.of(456), LiIf.<Integer, Integer>of(123)._case(123).then(i -> 456)._else(() -> 789));
        Assertions.assertEquals(Lino.of(789), LiIf.<Integer, Integer>of(1)._case(123).then(i -> 456)._else(() -> 789));

        Assertions.assertEquals(Lino.of(789), LiIf.<Integer, Integer>of(null)._case(1, i -> 456)._else(() -> 789));
        Assertions.assertEquals(Lino.of(456), LiIf.<Integer, Integer>of(123)._case(123, i -> 456)._else(() -> 789));
        Assertions.assertEquals(Lino.of(789), LiIf.<Integer, Integer>of(1)._case(123, i -> 456)._else(() -> 789));

        Assertions.assertEquals(Lino.of(789), LiIf.<Integer, Integer>of(null)._case(1, 2).then(i -> 456)._else(() -> 789));
        Assertions.assertEquals(Lino.of(456), LiIf.<Integer, Integer>of(123)._case(123, 456).then(i -> 456)._else(() -> 789));
        Assertions.assertEquals(Lino.of(789), LiIf.<Integer, Integer>of(1)._case(123, 456).then(i -> 456)._else(() -> 789));

        Assertions.assertEquals(Lino.of(789), LiIf.<Integer, Integer>of(1)._case((Integer) null).then(i -> 456)._else(() -> 789));
        Assertions.assertEquals(Lino.of(789), LiIf.<Integer, Integer>of(1)._case((Integer[]) null).then(i -> 456)._else(() -> 789));

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

    @Test
    public void _else() {

        Assertions.assertSame(Lino.none(), LiIf.of()._else());
    }

    @Test
    public void then() {

        Assertions.assertEquals(Lino.of(2), LiIf.of(1)._case(1).then(2)._else(3));
        Assertions.assertEquals(Lino.of(2), LiIf.of(1)._case(1).then(() -> 2)._else(3));


        Lino<String[]> strs = Lino.of(new String[]{});


//        LiIf<? super Integer, ? extends Integer> liIf = strs.map(a -> a.length).toIf();
//        liIf._case(1).then(()->2);
        int o = strs.map(a -> a.length).<Integer>toIf()
                ._case(1).then(1)
                ._case(2).then(2)
                ._else(3).get();


    }

}
