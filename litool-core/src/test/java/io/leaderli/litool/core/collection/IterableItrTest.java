package io.leaderli.litool.core.collection;

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
        Assertions.assertSame(NoneItr.class, IterableItr.ofs((Object[]) null).getClass());

        obj = NoneItr.of();
        Assertions.assertSame(NoneItr.class, IterableItr.of(obj).getClass());

        obj = Collections.emptyList();
        Assertions.assertSame(NoneItr.class, IterableItr.of(obj).getClass());

        obj = IterableItr.ofs(1, 2);
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
        Assertions.assertSame(NoneItr.class, IterableItr.of(obj).getClass());

        IterableItr<String> of = IterableItr.of(1);


        obj = new HashMap<>();
        Assertions.assertSame(NoneItr.class, IterableItr.of(obj).getClass());
        ((HashMap<String, String>) obj).put("1", "1");
        Assertions.assertSame(ArrayItr.class, IterableItr.of(obj).getClass());


        Object temp = obj;
        Assertions.assertThrows(ClassCastException.class, () -> {
            IterableItr<String> cast = IterableItr.of(temp);
            String s = cast.nextElement();
        });


        // enumeration
        Assertions.assertSame(IterableItr.ofs((Object[]) null), NoneItr.of());
        Assertions.assertTrue(IterableItr.of(enumeration()).hasNext());


        Enumeration<Integer> enumeration = IterableItr.ofs(1, 2, 3);
        Assertions.assertNotNull(enumeration.nextElement());
        Assertions.assertNotNull(enumeration.nextElement());
        Assertions.assertNotNull(enumeration.nextElement());
        Assertions.assertFalse(enumeration.hasMoreElements());

        // ArrayItr

        IterableItr<?> arrItr = IterableItr.ofs((Object[]) null);
        Assertions.assertSame(NoneItr.of(), arrItr);
        arrItr = IterableItr.ofs();
        Assertions.assertSame(NoneItr.of(), arrItr);

        arrItr = IterableItr.ofs((Object) null);
        Assertions.assertNull(arrItr.next());
        Assertions.assertSame(ArrayItr.class, arrItr.getClass());

        Object[] elements = {};
        Assertions.assertSame(IterableItr.ofs(elements), NoneItr.of());

        // Stream

        Assertions.assertEquals(1, IterableItr.of(Stream.of(1, 2)).next());

        //IterableItr
        Assertions.assertSame(IterableItr.of(NoneItr.of()), NoneItr.of());
        IterableItr<Integer> ofs = IterableItr.ofs(1);
        Assertions.assertNotSame(IterableItr.of(ofs), ofs);
        Assertions.assertEquals(IterableItr.of(ofs), ofs);

    }


    @Test
    void hasNext() {
        // ArrayItr
        IterableItr<Integer> arrItr = IterableItr.ofs(1, 2);
        Assertions.assertTrue(arrItr.hasNext());
        arrItr.forEachRemaining(s -> {

        });
        Assertions.assertFalse(arrItr.hasNext());

        // NoneItr
        Assertions.assertFalse(NoneItr.of().hasNext());

        // EnumerationItr
        IterableItr<Integer> enumerationItr = IterableItr.of(enumeration());
        Assertions.assertTrue(enumerationItr.hasNext());
        enumerationItr.next();
        Assertions.assertFalse(enumerationItr.hasNext());
    }

    @Test
    void next() {

        // arrItr
        IterableItr<?> arrItr = IterableItr.ofs(1, 2, 3);

        Assertions.assertEquals(1, arrItr.next());
        Assertions.assertEquals(2, arrItr.next());
        Assertions.assertEquals(3, arrItr.next());


        Assertions.assertThrows(NoSuchElementException.class, arrItr::next);


        // EnumerationItr
        IterableItr<Integer> enumerationItr = IterableItr.of(enumeration());
        enumerationItr.next();
        Assertions.assertThrows(NoSuchElementException.class, enumerationItr::next);

    }

    @Test
    void remove() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> IterableItr.of(1).remove());
    }

    @Test
    void iterator() {


        IterableItr<Integer> ofs = IterableItr.ofs(1);


        Assertions.assertEquals(ofs, ofs.iterator());
        Assertions.assertEquals(ofs, ofs.iterable());
        Assertions.assertEquals(ofs, ofs.enumeration());

        Assertions.assertNotSame(ofs, ofs.iterator());
        Assertions.assertNotSame(ofs, ofs.iterable());
        Assertions.assertNotSame(ofs, ofs.enumeration());

        Assertions.assertSame(NoneItr.of(), NoneItr.of().iterator());

        Iterator<Integer> iterator = ofs.iterator();
        iterator.next();
        iterator = ofs.iterator();
        Assertions.assertTrue(iterator.hasNext());


    }

    @Test
    void hasMoreElements() {
        Assertions.assertFalse(NoneItr.of().hasMoreElements());
        Assertions.assertTrue(IterableItr.ofs(1).hasMoreElements());
    }

    @Test
    void nextElement() {
        Assertions.assertThrows(NoSuchElementException.class, () -> NoneItr.of().nextElement());
        Assertions.assertEquals(1, IterableItr.ofs(1).nextElement());
    }

    private Enumeration<Integer> enumeration() {

        Vector<Integer> vector = new Vector<>(1);
        vector.add(1);
        return vector.elements();
    }


    @Test
    void absent() {


        Assertions.assertTrue(IterableItr.ofs((Object[]) null).absent());
        Assertions.assertTrue(IterableItr.of(Collections.emptyList()).absent());
        Assertions.assertTrue(IterableItr.of(Collections.singletonList(1)).present());
    }

    @Test
    void toList() {

        Assertions.assertTrue(IterableItr.ofs((Object[]) null).toList().isEmpty());
        Assertions.assertFalse(IterableItr.ofs(1, 2).toList().isEmpty());

        List<Integer> of = CollectionUtils.toList(1);

        Assertions.assertNotSame(of, IterableItr.of(of).toList());
        Assertions.assertNotSame(of, IterableItr.of(of.iterator()).toList());
        Assertions.assertEquals(of, IterableItr.of(of).toList());
        Assertions.assertEquals(of, IterableItr.of(of.iterator()).toList());

    }

    @Test
    void toArray() {

        Assertions.assertEquals(0, IterableItr.ofs().toArray(Object.class).length);
        Assertions.assertEquals(1, IterableItr.ofs(1).toArray().length);
        IterableItr<Integer> ofs = IterableItr.ofs(1, 2);
        Assertions.assertNotSame(ofs.toArray(), ofs.toArray());
        Assertions.assertArrayEquals(ofs.toArray(), ofs.toArray());

        Assertions.assertSame(Integer[].class, ofs.toArray().getClass());
        ofs = IterableItr.ofs();
        Assertions.assertSame(Object[].class, ofs.toArray().getClass());

        //noinspection AssertBetweenInconvertibleTypes

        Integer[] ints = {1, 2, 3};
        Assertions.assertArrayEquals(ints, IterableItr.ofs(ints).toArray());
        Assertions.assertArrayEquals(ints, IterableItr.ofs(ints).toArray(Integer.class));
        Assertions.assertArrayEquals(ints, IterableItr.ofs(ints).toArray(int.class));
        Assertions.assertNotSame(ints, IterableItr.ofs(ints).toArray());
        IterableItr<Integer> ofs1 = IterableItr.ofs(1);
        Object eq = 1;
        //noinspection EqualsBetweenInconvertibleTypes
        ofs1.equals(eq);
        Assertions.assertEquals("ArrayItr", ofs1.name());


    }
}
