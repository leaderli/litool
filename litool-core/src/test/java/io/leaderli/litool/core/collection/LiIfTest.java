package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.meta.LiIf;
import io.leaderli.litool.core.meta.Lino;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/6/24
 */
class LiIfTest {


    @Test
    void test_if() {


        Assertions.assertEquals(Lino.of(789),
                LiIf.<Integer, Integer>of(null)._if(i -> i == 123)._then(i -> 456)._else(() -> 789));
        Assertions.assertEquals(Lino.of(456),
                LiIf.<Integer, Integer>of(123)._if(i -> i == 123)._then(i -> 456)._else(() -> 789));
        Assertions.assertEquals(Lino.of(789),
                LiIf.<Integer, Integer>of(1)._if(i -> i == 123)._then(i -> 456)._else(() -> 789));

    }

    @Test
    void test_instanceof() {


        Assertions.assertEquals(Lino.of(789),
                LiIf.<Integer, Integer>of(null)._instanceof(Integer.class)._then(i -> 456)._else(() -> 789));
        Assertions.assertEquals(Lino.of(456),
                LiIf.<Integer, Integer>of(123)._instanceof(Integer.class)._then(i -> 456)._else(() -> 789));
        Assertions.assertEquals(Lino.of(789),
                LiIf.<Object, Integer>of(1)._instanceof(String.class)._then(i -> 456)._else(() -> 789));


        Assertions.assertEquals(Lino.of(789),
                LiIf.<Object, Integer>of(1)._instanceof((Class<?>) null)._then(i -> 456)._else(() -> 789));

    }

    @Test
    void test_case() {


        Assertions.assertEquals(Lino.of(789),
                LiIf.<Integer, Integer>of(null)._case(1)._then(i -> 456)._else(() -> 789));
        Assertions.assertEquals(Lino.of(456),
                LiIf.<Integer, Integer>of(123)._case(123)._then(i -> 456)._else(() -> 789));
        Assertions.assertEquals(Lino.of(789), LiIf.<Integer, Integer>of(1)._case(123)._then(i -> 456)._else(() -> 789));


        Assertions.assertEquals(Lino.of(789),
                LiIf.<Integer, Integer>of(null)._case(1, 2)._then(i -> 456)._else(() -> 789));
        Assertions.assertEquals(Lino.of(456),
                LiIf.<Integer, Integer>of(123)._case(123, 456)._then(i -> 456)._else(() -> 789));
        Assertions.assertEquals(Lino.of(789),
                LiIf.<Integer, Integer>of(1)._case(123, 456)._then(i -> 456)._else(() -> 789));

        Assertions.assertEquals(Lino.of(789),
                LiIf.<Integer, Integer>of(1)._case((Integer) null)._then(i -> 456)._else(() -> 789));
        Assertions.assertEquals(Lino.of(789),
                LiIf.<Integer, Integer>of(1)._case((Integer[]) null)._then(i -> 456)._else(() -> 789));

        LiIf.of(1)._case(1)._then(i -> {
            if (i == 1) {
                throw new RuntimeException();
            }
            return "1";
        });


        Assertions.assertThrows(RuntimeException.class, () ->
                LiIf.of(1)
                        ._case(1)._then(i -> {
                            if (i == 1) {
                                throw new RuntimeException();
                            }
                            return "1";
                        })
                        ._else(() -> 2)
        );
    }


    @Test
    void then() {

        Assertions.assertEquals(Lino.of(2), LiIf.of(1)._case(1)._then(2)._else(3));
        Assertions.assertEquals(Lino.of(2), LiIf.of(1)._case(1)._then(() -> 2)._else(3));


        Lino<String[]> strs = Lino.of(new String[]{});


//        LiIf<? super Integer, ? extends Integer> liIf = strs.map(a -> a.length).toIf();
//        liIf._case(1).then(()->2);
        int o = strs.map(a -> a.length).<Integer>toIf()
                ._case(1)._then(1)
                ._case(2)._then(2)
                ._else(3).get();


    }

}
