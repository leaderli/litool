package io.leaderli.litool.core.meta;

import io.leaderli.litool.core.exception.LiAssertUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

/**
 * @author leaderli
 * @since 2022/7/16
 */
class LiLinkTest {


    @Test
    void of() {

        LiBox<Integer> t1 = LiBox.none();
        LiBox<Integer> e1 = LiBox.none();
        LiBox<Integer> e2 = LiBox.none();

        SomeLink.of()
                .then(() -> t1.value(1))
                .error(LiAssertUtil::assertNotHere)
                .then(() -> 0)
                .then(LiAssertUtil::assertNotHere)
                .error(() -> e1.value(1))
                .error(() -> e2.value(1))
                .present();

        Assertions.assertEquals(t1.value(), 1);
        Assertions.assertEquals(e1.value(), 1);
        Assertions.assertEquals(e2.value(), 1);


        e1.reset();

        SomeLink.of(null)
                .error(v -> LiAssertUtil.assertNotHere()).error(() -> e1.value(1))
                .present();
        Assertions.assertEquals(e1.value(), 1);


        e1.reset();
        e2.reset();
        SomeLink.of(10)
                .then(() -> 0)
                .error((Consumer<Integer>) e1::value)
                .error(() -> e2.value(1))
                .onFinally(LiAssertUtil::assertFalse);

        Assertions.assertEquals(e1.value(), 10);
        Assertions.assertEquals(e2.value(), 1);


        SomeLink.of().onFinally(LiAssertUtil::assertTrue);
        SomeLink.of(null).onFinally(LiAssertUtil::assertFalse);


        LiConstant.temporary(
                () -> SomeLink.of()
                        .throwable_then(i -> {
                            System.out.println(i / (i - 1));
                            return true;
                        })
                        .onFinally(LiAssertUtil::assertFalse));

        Assertions.assertThrows(ArithmeticException.class,
                () -> SomeLink.of()
                        .then(i -> i / (i - 1))
                        .onFinally(LiAssertUtil::assertFalse));

        Assertions.assertDoesNotThrow(
                () -> {
                    SomeLink.of()
                            .then(i -> i / (i - 1));
                });
    }
}
