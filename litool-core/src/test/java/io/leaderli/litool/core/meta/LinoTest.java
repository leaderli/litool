package io.leaderli.litool.core.meta;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author leaderli
 * @since 2022/6/16
 */
class LinoTest {



    @Test
    void unzip() {

        Assertions.assertEquals(1, Lino.of(1).unzip(l -> Lino.of(1)).get());
        Assertions.assertNull(Lino.of(1).filter(i -> i > 2).unzip(l -> Lino.of(1)).get());

    }

    @Test
    void narrow() {
        Lino<CharSequence> narrow = Lino.narrow(Lino.<String>of(null));
        Assertions.assertSame(narrow, Lino.none());

        Lino<CharSequence> narrow1 = Lino.narrow(Lino.of("123"));
        Assertions.assertSame(narrow1.get(), "123");

        Lino<String> cast = narrow1.cast(String.class);

        Assertions.assertSame(cast.get(), "123");
        Lino<Integer> integerLino = narrow1.cast(Integer.class);
        Assertions.assertSame(integerLino, Lino.none());
    }


    @Test
    void none() {
        Assertions.assertTrue(Lino.of(null).absent());
        Assertions.assertFalse(Lino.of(1).absent());
    }

    @Test
    void cast() {
        Assertions.assertSame(Lino.none(), Lino.of(null).cast(String.class).cast(Integer.class));
        Assertions.assertSame(Lino.none(), Lino.of("1").cast(null));
        Object a = 1;
        Assertions.assertSame(1, Lino.of(a).cast(Integer.class).get());

        Assertions.assertEquals("[1]", Arrays.toString(Lino.of(new String[]{"1"}).cast(Object[].class).get()));

        Map<Object, Object> map = new HashMap<>();
        map.put("1", "1");
        map.put("2", 2);

        a = map;
        Assertions.assertEquals("{1=1}", Lino.of(a).cast(String.class, String.class).get().toString());
        Assertions.assertSame(Lino.none(), Lino.of(a).cast(Integer.class, String.class));


        Assertions.assertEquals(2, Lino.of(Arrays.asList(1, 2, 3)).cast(List.class).toLira(Object.class).get(1).get());


        Assertions.assertEquals(1, Lino.of(new int[]{1, 2}).toLira(Object.class).first().get());
    }

    @Test
    public void filter() {

        Lino<Boolean> simple = Lino.of(false);

        Assertions.assertTrue(simple.present());
        simple = simple.filter();
        Assertions.assertTrue(simple.absent());
        Lino<String> mono = Lino.of(null);

        Assertions.assertNull(mono.filter(Objects::nonNull).get());
        Assertions.assertNull(mono.filter(str -> str.length() == 4).get());

        mono = Lino.of("123");
        Assertions.assertNull(mono.filter(false).get());
        Assertions.assertNotNull(mono.filter(true).get());
        Assertions.assertNotNull(mono.filter(Objects::nonNull).get());
        Assertions.assertNull(mono.filter(str -> str.length() == 4).get());

//
        mono = Lino.of("123");
        Assertions.assertNotNull(mono.filter(Lino::of).get());


        Assertions.assertNull(mono.filter(it -> null).get());
        Assertions.assertNotNull(mono.filter(it -> 1).get());


        Assertions.assertNull(mono.filter(it -> new ArrayList<>()).get());
        Assertions.assertNotNull(mono.filter(it -> Arrays.asList(1, 2)).get());

        HashMap<Object, Object> test = new HashMap<>();
        Assertions.assertNull(mono.filter(it -> test).get());
        test.put("key", "value");
        Assertions.assertNotNull(mono.filter(it -> test).get());


    }

    @Test
    void same() {
        Assertions.assertTrue(Lino.of("1").contain("1").present());
        Assertions.assertTrue(Lino.of("2").contain("1").absent());
        Assertions.assertTrue(Lino.of(null).contain("1").absent());
    }

    @Test
    void of() {
        Assertions.assertTrue(Lino.of(null).absent());
        Assertions.assertFalse(Lino.of(1).absent());
        Assertions.assertTrue(Lino.of(() -> null).absent());
        Assertions.assertFalse(Lino.of(() -> 1).absent());
    }

    @Test
    void get() {
        Assertions.assertNotNull(Lino.of(1).get());
        Assertions.assertNull(Lino.of(null).get());

        Assertions.assertEquals(1, Lino.of(1).get(2));
        Assertions.assertEquals(2, Lino.of(null).get(2));
    }

    @Test
    void getOrElse() {
        Assertions.assertSame(1, Lino.of(null).or(1).get());
        Assertions.assertSame(2, Lino.of(2).or(1).get());
        Assertions.assertNotSame(1, Lino.of(2).or(1).get());
    }

    @Test
    void testGetOrElse() {
        Assertions.assertSame(1, Lino.of(null).or(() -> 1).get());
        Assertions.assertSame(2, Lino.of(2).or(() -> 1).get());
        Assertions.assertNotSame(1, Lino.of(2).or(() -> 1).get());
    }


    @Test
    void isPresent() {
        Lino.of(null).ifPresent(e -> Assertions.fail());

        Lino.of(null)
                .ifPresent(e -> {
                }).ifAbsent(() -> {
                }).absent();

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
        Lino.of(1).ifAbsent(Assertions::fail);
    }

    @Test
    public void equals() {

        Assertions.assertEquals(Lino.of(1), Lino.of(1));
        Assertions.assertNotEquals(Lino.of(2), Lino.of(1));
        Assertions.assertNotEquals(Lino.of(2), null);
        Assertions.assertNotEquals(Lino.of(2), 2);
        Assertions.assertSame(Lino.of(null), Lino.none());

    }

    @Test
    void map() {

        Assertions.assertSame(Lino.of(1).map(Integer::doubleValue).get().getClass(), Double.class);
        Assertions.assertSame(Lino.of(1).map(i -> null), Lino.none());
    }


    @Test
    void throwable_map() {
        LiBox<Integer> box = LiBox.none();
        Assertions.assertSame(Lino.of(0).throwable_map(i -> 5 / i, null), Lino.none());
        Assertions.assertNull(box.value());
        Lino.of(0).throwable_map(i -> 5 / i, t -> box.value(2));
        Assertions.assertSame(2, box.value());
        Assertions.assertThrows(ArithmeticException.class, () -> Lino.of(0).map(i -> 5 / i));
    }

    @Test
    void toLira() {


        String[] value = {"1", "2"};
        Assertions.assertEquals("1", Lino.of(value).toLira(CharSequence.class).getRaw().get(0));


        Assertions.assertSame(Lino.of(null).toLira(Object.class), Lira.none());

        Assertions.assertEquals(Lira.of(1).get(), Lino.of(1).toLira(Integer.class).get());


//
        Lino<List<Integer>> of = Lino.of(Arrays.asList(1, 2));
        Lira<Integer> lira = of.toLira(Integer.class);
        Assertions.assertEquals(2, lira.size());

        Assertions.assertEquals("[1, 2]", Lino.of(Arrays.asList(1, 2)).toLira(Integer.class).get().toString());
        Assertions.assertEquals("[]", Lino.of(Arrays.asList(1, 2)).toLira(String.class).get().toString());


        Assertions.assertEquals(1, Lino.of(new int[]{1, 2}).toLira(Object.class).getRaw().get(0));
        Assertions.assertEquals(1, Lino.of(1).toLira(Object.class).getRaw().get(0));


    }

    @Test
    void assertNotNone() {

        Lino.of(1).assertNotNone("123");

        IllegalStateException assertThrows = Assertions.assertThrows(IllegalStateException.class, () -> Lino.of(null).assertNotNone("haha"));
        Assertions.assertEquals("haha", assertThrows.getMessage());


    }

    @Test
    void tuple() {


        Assertions.assertEquals(3, Lino.of("123").tuple(String::length).get()._2);
        Assertions.assertNull(Lino.of("123").tuple(s -> null).map(LiTuple2::_2).get());
        Assertions.assertNull(Lino.of((String) null).tuple(String::length).get());


    }


}
