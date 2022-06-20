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
    void cast() {
        Assertions.assertSame(Lino.none(), Lino.of(null).cast(String.class).cast(Integer.class));
        Assertions.assertSame(Lino.none(), Lino.of("1").cast(null));
        Object a = 1;
        Assertions.assertSame(1, Lino.of(a).cast(Integer.class).get());

        Assertions.assertEquals("[1]", Arrays.toString(Lino.of(new String[]{"1"}).cast(Object[].class).get()));

        Map<Object,Object> map = new HashMap<>();
        map.put("1","1");
        map.put("2",2);

        a = map;
        Assertions.assertEquals("{1=1}",Lino.of(a).cast(String.class, String.class).get().toString());
        Assertions.assertSame(Lino.none(),Lino.of(a).cast(Integer.class, String.class));
    }

    @Test
    public void filter() {

        Lino<Boolean> simple = Lino.of(false);

        Assertions.assertTrue(simple.isPresent());
        simple = simple.filter();
        Assertions.assertTrue(simple.notPresent());
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
        Assertions.assertTrue(Lino.of("1").same("1").isPresent());
        Assertions.assertTrue(Lino.of("2").same("1").notPresent());
        Assertions.assertTrue(Lino.of(null).same("1").notPresent());
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

    @Test
    void map() {

        Assertions.assertSame(Lino.of(1).map(Integer::doubleValue).get().getClass(), Double.class);
        Assertions.assertSame(Lino.of(1).map(i -> null), Lino.none());
    }


    @Test
    void throwable_map() {
        LiBox<Integer> box = LiBox.None();
        Assertions.assertSame(Lino.of(0).throwable_map(i -> 5 / i), Lino.none());
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

        Assertions.assertEquals(Lira.of(1), Lino.of(1).toLira(Integer.class));


//
        Lino<List<Integer>> of = Lino.of(Arrays.asList(1, 2));
        Lira<Integer> lira = of.toLira(Integer.class);
        Assertions.assertEquals(2, lira.size());

        Assertions.assertEquals("List[Some(1), Some(2)]", Lino.of(Arrays.asList(1, 2)).toLira(Integer.class).toString());
        Assertions.assertEquals("Empty[]", Lino.of(Arrays.asList(1, 2)).toLira(String.class).toString());
    }
}