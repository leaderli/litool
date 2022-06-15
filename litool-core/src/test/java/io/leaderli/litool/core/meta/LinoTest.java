package io.leaderli.litool.core.meta;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author leaderli
 * @since 2022/6/16
 */
class LinoTest {

    @Test
    void narrow() {
        Lino.none().ifPresent(r -> System.out.println());
        Lino<CharSequence> narrow = Lino.narrow(Lino.<String>of(null));
        Assertions.assertSame(narrow, Lino.none());

    }

    @Test
    void none() {
        Assertions.assertTrue(Lino.of(null).notPresent());
        Assertions.assertFalse(Lino.of(1).notPresent());
    }

    @Test
    void of() {
        Assertions.assertTrue(Lino.of(null).notPresent());
        Assertions.assertFalse(Lino.of(1).notPresent());
    }

    @Test
    void get() {
        Assertions.assertNotNull(Lino.of(1).get());
        Assertions.assertNull(Lino.of(null).get());
    }

    @Test
    void getOrElse() {
        Assertions.assertSame(1, Lino.of(null).getOrElse(1));
        Assertions.assertSame(2, Lino.of(2).getOrElse(1));
        Assertions.assertNotSame(1, Lino.of(2).getOrElse(1));
    }

    @Test
    void testGetOrElse() {
        Assertions.assertSame(1, Lino.of(null).getOrElse(() -> 1));
        Assertions.assertSame(2, Lino.of(2).getOrElse(() -> 1));
        Assertions.assertNotSame(1, Lino.of(2).getOrElse(() -> 1));
    }


    @Test
    void isPresent() {
        Lino.of(null).ifPresent(e -> Assertions.fail());

        Lino.of(null).ifPresent(e -> {
        }).ifNotPresent(() -> {
        }).notPresent();

        Throwable cause = Assertions.assertThrows(RuntimeException.class, () -> Lino.of(0).ifPresent(in -> {
            @SuppressWarnings("unused") int i = 1 / in;

        }));

        Assertions.assertEquals("/ by zero", cause.getMessage());
    }

    @Test
    public void ifThrowablePresent() {
        Throwable cause = Assertions.assertThrows(RuntimeException.class, () -> Lino.of(0).ifThrowablePresent(in -> {
            TimeUnit.MICROSECONDS.sleep(1);
            @SuppressWarnings("unused") int i = 1 / in;

        })).getCause();

        Assertions.assertEquals("/ by zero", cause.getMessage());

    }

    @Test
    void notPresent() {
        Lino.of(1).ifNotPresent(Assertions::fail);
    }

    @Test
    public void equals() {

        Assertions.assertEquals(Lino.of(1), Lino.of(1));
        Assertions.assertNotEquals(Lino.of(2), Lino.of(1));
        Assertions.assertNotEquals(Lino.of(2), null);
        Assertions.assertNotEquals(Lino.of(2), 2);
        Assertions.assertSame(Lino.of(null), Lino.none());

    }
}
