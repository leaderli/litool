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

        LiLink.of()
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

        LiLink.none()
                .error(v -> LiAssertUtil.assertNotHere()).error(() -> e1.value(1))
                .present();
        Assertions.assertEquals(e1.value(), 1);


        e1.reset();
        e2.reset();
        LiLink.of(10)
                .then(() -> 0)
                .error((Consumer<Integer>) e1::value)
                .error(() -> e2.value(1))
                .onFinally(LiAssertUtil::assertFalse);

        Assertions.assertEquals(e1.value(), 10);
        Assertions.assertEquals(e2.value(), 1);


        LiLink.of().onFinally(LiAssertUtil::assertTrue);
        LiLink.none().onFinally(LiAssertUtil::assertFalse);


        LiConstant.temporary(
                () -> LiLink.of()
                        .throwable_then(i -> {
                            System.out.println(i / (i - 1));
                            return true;
                        })
                        .onFinally(LiAssertUtil::assertFalse));

        Assertions.assertThrows(ArithmeticException.class,
                () -> LiLink.of()
                        .then(i -> i / (i - 1))
                        .onFinally(LiAssertUtil::assertFalse));

        Assertions.assertDoesNotThrow(
                () -> {
                    LiLink.of()
                            .then(i -> i / (i - 1));
                });
    }

    @Test
    void request() {
        Assertions.assertDoesNotThrow(() -> LiLink.none()
                .error(LiAssertUtil::assertNotHere)
                .request(1));
        Assertions.assertThrows(IllegalStateException.class, () -> LiLink.of(1)
                .error(LiAssertUtil::assertNotHere)
                .request(null));


        LiBox<Integer> t1 = LiBox.none();
        LiBox<Integer> e1 = LiBox.none();

        LiLink.<Integer>none()
                .error(LiAssertUtil::assertNotHere)
                .then((Consumer<? super Integer>) t1::value)
                .request(1);

        Assertions.assertEquals(t1.value(), 1);

        t1.value(0);
        LiLink.<Integer>none()
                .error(LiAssertUtil::assertNotHere)
                .then((Consumer<? super Integer>) v -> t1.value(t1.value() + v))
                .then((Consumer<? super Integer>) v -> t1.value(t1.value() + v))
                .request(1);

        Assertions.assertEquals(t1.value(), 2);

        e1.value(0);
        LiLink.of(1)
                .error(() -> e1.value(e1.value() + 1))
                .then((Consumer<? super Integer>) v -> t1.value(t1.value() + v))
                .error(() -> e1.value(e1.value() + 1))
                .then((Consumer<? super Integer>) v -> t1.value(t1.value() + v))
                .error(() -> e1.value(e1.value() + 1))
                .request(null);

        Assertions.assertEquals(1, e1.value());

        e1.value(0);
        LiLink.of(1)
                .then((Consumer<? super Integer>) v -> t1.value(t1.value() + v))
                .then((Consumer<? super Integer>) v -> t1.value(t1.value() + v))
                .error(() -> e1.value(e1.value() + 1))
                .error(() -> e1.value(e1.value() + 1))
                .error(() -> e1.value(e1.value() + 1))
                .request(null);

        Assertions.assertEquals(3, e1.value());

        t1.value(0);
        LiLink.<Integer>none()
                .error(LiAssertUtil::assertNotHere)
                .then((Consumer<? super Integer>) v -> t1.value(t1.value() + v))
                .error(LiAssertUtil::assertNotHere)
                .then((Consumer<? super Integer>) v -> t1.value(t1.value() + v))
                .request(1);

        Assertions.assertEquals(t1.value(), 2);


        t1.value(0);
        LiLink.of(1)
                .then((Consumer<? super Integer>) v -> t1.value(0))
                .map(Object::toString)
                .error(LiAssertUtil::assertNotHere)
                .run();

        e1.value(0);
        LiLink.of(0)
                .then(v -> v)
                .map(Object::toString)
                .then(LiAssertUtil::assertNotHere)
                .error(() -> e1.value(1))
                .run();

        Assertions.assertEquals(e1.value(), 1);


        e1.value(0);
        LiLink.of(1)
                .then((Consumer<? super Integer>) v -> t1.value(0))
                .error(LiAssertUtil::assertNotHere)
                .map(Object::toString)
                .then(v -> 0)
                .error(() -> e1.value(1))
                .run();
        Assertions.assertEquals(e1.value(), 1);


        e1.value(0);
        LiLink.of(1)
                .then((Consumer<? super Integer>) v -> t1.value(0))
                .error(LiAssertUtil::assertNotHere)
                .union("1")
                .then(v -> 0)
                .error(() -> e1.value(1))
                .run();
        Assertions.assertEquals(e1.value(), 1);

    }
}
