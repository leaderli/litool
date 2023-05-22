package io.leaderli.litool.core.collection;

import io.leaderli.litool.core.collection.IterableItr.ArrayItr;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author leaderli
 * @since 2022/8/27
 */
class IterableItrTest {


    @SuppressWarnings("unchecked")
    @Test
    void of() {

        // obj

        Object obj;
        Assertions.assertSame(IterableItr.NoneItr.class, IterableItr.of(null).getClass());

        obj = IterableItr.NoneItr.of();
        Assertions.assertSame(IterableItr.NoneItr.class, IterableItr.of(obj).getClass());

        obj = Collections.emptyList();
        Assertions.assertSame(IterableItr.NoneItr.class, IterableItr.of(obj).getClass());

        obj = IterableItr.fromArray(1, 2);
        Assertions.assertSame(ArrayItr.class, IterableItr.of(obj).getClass());


        obj = Arrays.asList(1, 1);
        Assertions.assertSame(ArrayItr.class, IterableItr.of(obj).getClass());


        obj = Stream.of(1, 1);
        Assertions.assertSame(ArrayItr.class, IterableItr.of(obj).getClass());
        obj = enumeration();
        Assertions.assertSame(ArrayItr.class, IterableItr.of(obj).getClass());

        obj = new int[]{1, 2};
        Assertions.assertSame(ArrayItr.class, IterableItr.of(obj).getClass());

        obj = 1;
        Assertions.assertSame(IterableItr.NoneItr.class, IterableItr.of(obj).getClass());

        IterableItr<String> of = IterableItr.of(1);


        obj = new HashMap<>();
        Assertions.assertSame(IterableItr.NoneItr.class, IterableItr.of(obj).getClass());
        ((HashMap<String, String>) obj).put("1", "1");
        Assertions.assertSame(ArrayItr.class, IterableItr.of(obj).getClass());

        // generator
        obj = Generators.range();
        Assertions.assertSame(IntGenerator.class, IterableItr.of(obj).getClass());


        Object temp = obj;
        Assertions.assertThrows(ClassCastException.class, () -> {
            IterableItr<String> cast = IterableItr.of(temp);
            String s = cast.nextElement();
        });


        // enumeration
        Assertions.assertSame(IterableItr.of(null), IterableItr.NoneItr.of());
        Assertions.assertTrue(IterableItr.fromEnumeration(enumeration()).hasNext());


        Enumeration<Integer> enumeration = IterableItr.fromArray(1, 2, 3);
        Assertions.assertNotNull(enumeration.nextElement());
        Assertions.assertNotNull(enumeration.nextElement());
        Assertions.assertNotNull(enumeration.nextElement());
        Assertions.assertFalse(enumeration.hasMoreElements());

        // ArrayItr

        IterableItr<?> arrItr = IterableItr.fromArray((Object[]) null);
        Assertions.assertSame(IterableItr.NoneItr.of(), arrItr);
        arrItr = IterableItr.fromArray();
        Assertions.assertSame(IterableItr.NoneItr.of(), arrItr);

        arrItr = IterableItr.fromArray((Object) null);
        Assertions.assertNull(arrItr.next());
        Assertions.assertSame(ArrayItr.class, arrItr.getClass());

        Object[] elements = {};
        Assertions.assertSame(IterableItr.fromArray(elements), IterableItr.NoneItr.of());

        // Stream

        Assertions.assertEquals(1, IterableItr.fromStream(Stream.of(1, 2)).next());

    }


    @Test
    void hasNext() {
        // ArrayItr
        IterableItr<Integer> arrItr = IterableItr.fromArray(1, 2);
        Assertions.assertTrue(arrItr.hasNext());
        arrItr.forEachRemaining(s -> {

        });
        Assertions.assertFalse(arrItr.hasNext());

        // NoneItr
        Assertions.assertFalse(IterableItr.NoneItr.of().hasNext());

        // EnumerationItr
        IterableItr<Integer> enumerationItr = IterableItr.fromEnumeration(enumeration());
        Assertions.assertTrue(enumerationItr.hasNext());
        enumerationItr.next();
        Assertions.assertFalse(enumerationItr.hasNext());
    }

    @Test
    void next() {

        // arrItr
        IterableItr<?> arrItr = IterableItr.fromArray(1, 2, 3);

        Assertions.assertEquals(1, arrItr.next());
        Assertions.assertEquals(2, arrItr.next());
        Assertions.assertEquals(3, arrItr.next());


        Assertions.assertThrows(NoSuchElementException.class, arrItr::next);


        // EnumerationItr
        IterableItr<Integer> enumerationItr = IterableItr.fromEnumeration(enumeration());
        enumerationItr.next();
        Assertions.assertThrows(NoSuchElementException.class, enumerationItr::next);

    }

    @Test
    void remove() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> IterableItr.fromArray(1).remove());
    }

    @Test
    void iterator() {


        IterableItr<Integer> ofs = IterableItr.fromArray(1);


        Assertions.assertEquals(ofs, ofs.iterator());
        Assertions.assertEquals(ofs, ofs.iterable());
        Assertions.assertEquals(ofs, ofs.enumeration());

        Assertions.assertNotSame(ofs, ofs.iterator());
        Assertions.assertNotSame(ofs, ofs.iterable());
        Assertions.assertNotSame(ofs, ofs.enumeration());

        Assertions.assertSame(IterableItr.NoneItr.of(), IterableItr.NoneItr.of().iterator());

        Iterator<Integer> iterator = ofs.iterator();
        iterator.next();
        iterator = ofs.iterator();
        Assertions.assertTrue(iterator.hasNext());


    }

    @Test
    void hasMoreElements() {
        Assertions.assertFalse(IterableItr.NoneItr.of().hasMoreElements());
        Assertions.assertTrue(IterableItr.fromArray(1).hasMoreElements());
    }

    @Test
    void nextElement() {
        Assertions.assertThrows(NoSuchElementException.class, () -> IterableItr.NoneItr.of().nextElement());
        Assertions.assertEquals(1, IterableItr.fromArray(1).nextElement());
    }

    private Enumeration<Integer> enumeration() {

        Vector<Integer> vector = new Vector<>(1);
        vector.add(1);
        return vector.elements();
    }


    @Test
    void absent() {


        Assertions.assertTrue(IterableItr.of(null).absent());
        Assertions.assertTrue(IterableItr.of(Collections.emptyList()).absent());
        Assertions.assertTrue(IterableItr.of(Collections.singletonList(1)).present());
    }

    @Test
    void toList() {

        Assertions.assertTrue(IterableItr.of(null).toList().isEmpty());
        Assertions.assertFalse(IterableItr.fromArray(1, 2).toList().isEmpty());
        Assertions.assertThrows(UnsupportedOperationException.class, () -> IterableItr.fromGenerator(new IntGenerator()).toList());
    }
}
