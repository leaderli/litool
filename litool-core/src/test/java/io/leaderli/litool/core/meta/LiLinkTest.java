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
                .interrupt(LiAssertUtil::assertNotRun)
                .then(() -> 0)
                .then(LiAssertUtil::assertNotRun)
                .interrupt(() -> e1.value(1))
                .interrupt(() -> e2.value(1))
                .present();

        Assertions.assertEquals(1, t1.value());
        Assertions.assertEquals(1, e1.value());
        Assertions.assertEquals(1, e2.value());


        e1.reset();

        LiLink.none()
                .interrupt(v -> LiAssertUtil.assertNotRun()).interrupt(() -> e1.value(1))
                .present();
        Assertions.assertEquals(1, e1.value());


        e1.reset();
        e2.reset();
        LiLink.of(10)
                .then(() -> 0)
                .interrupt((Consumer<Integer>) e1::value)
                .interrupt(() -> e2.value(1))
                .onFinally(LiAssertUtil::assertFalse);

        Assertions.assertEquals(10, e1.value());
        Assertions.assertEquals(1, e2.value());


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
        Assertions.assertDoesNotThrow(() -> LiLink.of(1)
                .interrupt(LiAssertUtil::assertNotRun)
                .run());
        Assertions.assertThrows(IllegalStateException.class, () -> LiLink.none()
                .interrupt(LiAssertUtil::assertNotRun)
                .run());


        LiBox<Integer> t1 = LiBox.none();
        LiBox<Integer> e1 = LiBox.none();

        LiLink.of(1)
                .interrupt(LiAssertUtil::assertNotRun)
                .then((Consumer<? super Integer>) t1::value)
                .run();

        Assertions.assertEquals(1, t1.value());

        t1.value(0);
        LiLink.of(1)
                .interrupt(LiAssertUtil::assertNotRun)
                .then((Consumer<? super Integer>) v -> t1.value(t1.value() + v))
                .then((Consumer<? super Integer>) v -> t1.value(t1.value() + v))
                .run();

        Assertions.assertEquals(2, t1.value());

        e1.value(0);
        LiLink.<Integer>of(null)
                .interrupt(() -> e1.value(e1.value() + 1))
                .then((Consumer<? super Integer>) v -> t1.value(t1.value() + v))
                .interrupt(() -> e1.value(e1.value() + 1))
                .then((Consumer<? super Integer>) v -> t1.value(t1.value() + v))
                .interrupt(() -> e1.value(e1.value() + 1))
                .run();

        Assertions.assertEquals(1, e1.value());

        e1.value(0);
        LiLink.of((Integer) null)
                .then((Consumer<? super Integer>) v -> t1.value(t1.value() + v))
                .then((Consumer<? super Integer>) v -> t1.value(t1.value() + v))
                .interrupt(() -> e1.value(e1.value() + 1))
                .interrupt(() -> e1.value(e1.value() + 1))
                .interrupt(() -> e1.value(e1.value() + 1))
                .run();

        Assertions.assertEquals(3, e1.value());

        t1.value(0);
        LiLink.of(1)
                .interrupt(LiAssertUtil::assertNotRun)
                .then((Consumer<? super Integer>) v -> t1.value(t1.value() + v))
                .interrupt(LiAssertUtil::assertNotRun)
                .then((Consumer<? super Integer>) v -> t1.value(t1.value() + v))
                .run();

        Assertions.assertEquals(2, t1.value());


        t1.value(0);
        LiLink.of(1)
                .then((Consumer<? super Integer>) v -> t1.value(0))
                .map(Object::toString)
                .interrupt(LiAssertUtil::assertNotRun)
                .run();

        e1.value(0);
        LiLink.of(0)
                .then(v -> v)
                .map(Object::toString)
                .then(LiAssertUtil::assertNotRun)
                .interrupt(() -> e1.value(1))
                .run();

        Assertions.assertEquals(1, e1.value());


        e1.value(0);
        LiLink.of(1)
                .then((Consumer<? super Integer>) v -> t1.value(0))
                .interrupt(LiAssertUtil::assertNotRun)
                .map(Object::toString)
                .then(v -> 0)
                .interrupt(() -> e1.value(1))
                .run();
        Assertions.assertEquals(1, e1.value());


        e1.value(0);
        LiLink.of(1)
                .then((Consumer<? super Integer>) v -> t1.value(0))
                .interrupt(LiAssertUtil::assertNotRun)
                .onFinally(r -> {
                    if (r) {
                        LiLink.of("1")
                                .then(v -> 0)
                                .interrupt(() -> e1.value(1))
                                .run();
                    }
                });
        Assertions.assertEquals(1, e1.value());

    }
}
