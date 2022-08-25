package io.leaderli.litool.core.meta;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/6/16
 */
class LiBoxTest {

    @Test
    void value() {

        LiBox<String> none = LiBox.none();
        assert none.lino().absent();

        none.value("123");
        assert none.lino().present();

    }

    @Test
    public void apply() {


        LiBox<String> str = new LiBox<>("hello");

        str.apply((s, s2) -> s + s2, "456");
        Assertions.assertEquals("hello456", str.value());

        str.apply(null, "123");
        Assertions.assertEquals("hello456", str.value());

        str.apply((s, s2) -> s + s2, null);
        Assertions.assertEquals("hello456", str.value());

        str.apply((s, s2) -> null, null);
        Assertions.assertEquals("hello456", str.value());

        str.apply((s, s2) -> null, "1");
        Assertions.assertTrue(str.absent());


        str.reset();

        str.apply(null, "hello");
        Assertions.assertTrue(str.absent());


        LiBox<Integer> num = LiBox.none();
        num.apply(Integer::sum, 2);
        Assertions.assertTrue(num.absent());


    }

}
