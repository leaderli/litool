package io.leaderli.litool.core.meta;

import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/6/16
 */
class LiBoxTest {

    @Test
    public void test() {

        LiBox<String> str = new LiBox<>("hello");


        LiBox<String> none = LiBox.none();
        assert none.lino().absent();
        none.value("123");
        assert none.lino().present();

    }

}
