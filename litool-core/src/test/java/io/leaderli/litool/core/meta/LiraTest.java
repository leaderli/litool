package io.leaderli.litool.core.meta;

import io.leaderli.litool.core.collection.Generator;
import io.leaderli.litool.core.collection.IterableItr;
import io.leaderli.litool.core.exception.LiAssertUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author leaderli
 * @since 2022/6/19
 */
class LiraTest {


@Test
void until() {

    Assertions.assertEquals(2, Lira.of(1, 2, 3).until(i -> i > 1).last().get());
    Assertions.assertEquals(2, Lira.of(1, null, 2, 3).until(i -> i > 1).last().get());

}

@Test
void untilNull() {
    Assertions.assertEquals(3, Lira.of(1, 2, 3).untilNull(i -> i > 1).last().get());
    Assertions.assertEquals(1, Lira.of(1, null, 2, 3).untilNull(i -> i > 1).last().get());


}

@Test
void nullable() {

    Assertions.assertArrayEquals(new Integer[]{100, 1}, Lira.of(null, 1).nullable(() -> 100).toArray());
    Assertions.assertArrayEquals(new Integer[]{1}, Lira.of(null, 1).filter().nullable(() -> 100).toArray());
    Assertions.assertEquals(1, Lira.of(1, null).size());

}

@Test
void getIndex() {

    Assertions.assertEquals(1, Lira.of(1, 2, 3).get(0).get());
    Assertions.assertEquals(2, Lira.of(1, 2, 3).get(1).get());
    Assertions.assertEquals(3, Lira.of(1, 2, 3).get(2).get());
    Assertions.assertEquals(Lino.none(), Lira.of(1, 2, 3).get(4));
}

@Test
void narrow() {

    Lira<CharSequence> narrow1 = Lira.narrow(Lira.of("123", "456"));
    Assertions.assertEquals("[123, 456]", narrow1.get().toString());

    Lira<String> cast = narrow1.cast(String.class);
    Assertions.assertEquals("[123, 456]", cast.get().toString());

}

@Test
void cast() {
    Map<Object, Object> map = new HashMap<>();
    map.put("1", "1");
    map.put("2", 2);

    @SuppressWarnings("UnnecessaryLocalVariable")
    Object obj = map;


    Assertions.assertEquals(1, Lira.of(map).cast(Map.class).size());
    Assertions.assertEquals(0, Lira.of(map).cast(Integer.class).size());

    Assertions.assertEquals("{1=1}", Lira.of(obj).cast(String.class, String.class).first().get().toString());
    Assertions.assertSame(Lino.none(), Lira.of(obj).cast(Integer.class, String.class).first());


}

@Test
void contains() {

    Assertions.assertFalse(Lira.of((Iterable<?>) null).contains(null));
    Assertions.assertFalse(Lira.of(1, 2).contains(null));
    Assertions.assertTrue(Lira.of(1, 2).contains(1));
    Assertions.assertTrue(Lira.of(1, 2).contains(2));


}

@Test
void of() {

    String[] ss = new String[]{"1", "2"};
    Assertions.assertSame(2, Lira.of(ss).size());

    Assertions.assertSame(1, Lira.of("1").size());
    Assertions.assertSame(3, Lira.of(Arrays.asList(1, 2, 3)).size());
    Assertions.assertSame(1, Lira.of("1", null).size());
    Assertions.assertSame(Lira.none(), Lira.of());

    Lira.of((Object) null);
    Assertions.assertSame(Lira.none(), Lira.of());
    Assertions.assertSame(Lira.none(), Lira.of((Object[]) null));
    Assertions.assertSame(Lira.none(), Lira.of(Collections.emptyIterator()));
    Assertions.assertSame(Lira.none(), Lira.of(Collections.emptyList()));
    Assertions.assertNotSame(Lira.none(), Lira.of(1));
    Assertions.assertNotSame(Lira.of(1), Lira.of(1));

    Assertions.assertEquals("[1, 2]", Lira.of("1", null, "2").get().toString());


    Lira<Integer> loop = Lira.of(() -> new Generator<Integer>() {
        int i = 0;

        @Override
        public Integer next() {
            return i++;
        }
    });

    Assertions.assertEquals(4, loop.limit(5).last().get());
    Assertions.assertEquals(4, loop.limit(5).last().get());


}

@Test
void filter() {

    Assertions.assertTrue(Lira.of(1, 2, 3).filter(i -> i > 4).absent());
    Assertions.assertSame(2, Lira.of(1, 2, 3).filter(i -> i > 1).size());
    Assertions.assertSame(2, Lira.of(1, 2, 3).filter(i -> i > 1).get().get(0));

    Lira.of(1, 2, 3).filter();
    Assertions.assertEquals(3, Lira.of(1, 2, 3).filter().size());
    Assertions.assertEquals(2, Lira.of(1, null, 3).filter().size());
    Assertions.assertEquals(0, Lira.of((Object) null).filter().size());
}

@Test
void reduce() {

    Assertions.assertEquals(6, Lira.of(1, 2, 3).reduce(Integer::sum).get());
    Assertions.assertEquals(6, Lira.of(null, 1, 2, 3).reduce(Integer::sum).get());
    Assertions.assertNull(Lira.of(null, 1, 2, 3).reduce(null).get());

    Assertions.assertEquals(6, Lira.of(1, 2, 3).reduce(0, Integer::sum).get());
    Assertions.assertEquals(6, Lira.of(1, 2, 3).reduce(null, Integer::sum).get());
    Assertions.assertEquals(7, Lira.of(1, 2, 3).reduce(1, Integer::sum).get());
    Assertions.assertNull(Lira.of(null, 1, 2, 3).reduce(1, null).get());
    Assertions.assertNull(Lira.of(1, 2, 3).reduce(1, (a, b) -> {
        if (b == 3) {
            return null;
        }
        return a + b;
    }).get());

}

@Test
void get() {

    Assertions.assertTrue(Lira.none().get().isEmpty());
    Assertions.assertFalse(Lira.of(1).get().isEmpty());
    Assertions.assertSame(2, Lira.of(1, 2).size());
}


@Test
void or() {

    Lira<String> none = Lira.none();

    Assertions.assertEquals("1", none.or("1").get().get(0));
    Assertions.assertEquals("1", Lira.of("1", "2").or(Arrays.asList("5", "4")).get().get(0));


    Assertions.assertSame(0, Lira.of(1, 2).filter(i -> i > 3).size());
    Assertions.assertSame(4, Lira.of(1, 2).filter(i -> i > 3).or(1, 2, 3, 4).size());
}


@Test
void skip() {

    Assertions.assertSame(0, Lira.of(1).skip(1).size());
    Assertions.assertSame(0, Lira.of().skip(1).size());
    Assertions.assertSame(1, Lira.of(1).skip(0).size());
    Assertions.assertSame(1, Lira.of(1, 2).skip(1).size());
    Assertions.assertSame(2, Lira.of(1, 2).skip(-1).size());
}


@Test
void sort() {

    Assertions.assertSame(1, Lira.of(2, 1).sorted().first().get());
    Assertions.assertSame(3, Lira.of(2, 1, 3).sorted((o1, o2) -> o2 - o1).first().get());

    Assertions.assertSame(2, Lira.of(2, 1).sorted().last().get());
    Assertions.assertSame(1, Lira.of(2, 1, 3).sorted((o1, o2) -> o2 - o1).last().get());
}

@Test
void limit() {

    Assertions.assertSame(1, Lira.of(1).limit(1).size());
    Assertions.assertSame(0, Lira.of().limit(1).size());
    Assertions.assertSame(1, Lira.of(1, 2).limit(1).size());
    Assertions.assertSame(2, Lira.of(1, 2).limit(-1).size());
    Assertions.assertSame(0, Lira.of(1, 2).limit(0).size());
}

@Test
void toMap() {


    Assertions.assertEquals("{1=2}", Lira.of(LiTuple.of(1, 2)).toMap(LiTuple2::_1, LiTuple2::_2).toString());
    Assertions.assertNull(Lira.of(LiTuple.of(1, null)).toMap(LiTuple2::_1, LiTuple2::_2).get(1));
    Assertions.assertEquals("{}", Lira.of(LiTuple.of(null, null)).toMap(LiTuple2::_1, LiTuple2::_2).toString());

    Assertions.assertEquals("{1=2}", Lira.of(LiTuple.of(1, 2)).toMap(l -> l).toString());
    Assertions.assertNull(Lira.of(LiTuple.of(1, null)).toMap(l -> l).get(1));
    Assertions.assertEquals("{}", Lira.of(LiTuple.of(null, null)).toMap(l -> l).toString());
}

@Test
void map() {

    System.out.println(Lira.of(1, 2, 0).throwable_map(i -> 4 / i));
    Assertions.assertTrue(Lira.of(1).map(i -> null).absent());
}

@Test
void toArray() {

    Assertions.assertDoesNotThrow(
            () -> {

                Number[] nums = Lira.of(1, 2, 3, 4.0).toArray(Integer.class);
                Number[] nums2 = Lira.of(1, 2, 3).toArray(int.class);
            }
    );
}

@Test
void set() {

    Lira<Integer> set = Lira.of(1, 2, 1, 2).filter(i -> {
        LiAssertUtil.assertNotRun();
        return true;
    }).distinct();

    Assertions.assertEquals(2, Lira.of(1, 2, 1, 2).distinct().size());


    Assertions.assertEquals(3, Lira.of(1, 2, 3, 4, 1).distinct().map(i -> i / 2).distinct().size());

    Assertions.assertEquals(1, Lira.of(1, 2, 3, 4, 1).distinct((left, right) -> left - right < 2).first().get());
}

@Test
void flatMap() {

    List<String> linos = Lira.of("1 2 3")
            .map(s -> s.split(" "))
            .flatMap(IterableItr::ofs)
            .get();


    Assertions.assertEquals(3, linos.size());
    linos = Lira.of("1 2 3")
            .map(s -> s.split(" "))
            .<String>flatMap()
            .filter(f -> !f.equals("2"))
            .get();
    Assertions.assertEquals(2, linos.size());

}


}
