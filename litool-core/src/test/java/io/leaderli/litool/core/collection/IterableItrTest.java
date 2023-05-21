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

        Object obj = null;
        Assertions.assertSame(NoneItr.class, IterableItr.of(obj).getClass());

        obj = NoneItr.of();
        Assertions.assertSame(NoneItr.class, IterableItr.of(obj).getClass());

        obj = IterableItr.ofs(1, 2);
        Assertions.assertSame(ArrayItr.class, IterableItr.of(obj).getClass());


        obj = Arrays.asList(1, 1);
        Assertions.assertSame(ArrayItr.class, IterableItr.of(obj).getClass());

        obj = Collections.emptyList();
        Assertions.assertSame(NoneItr.class, IterableItr.of(obj).getClass());

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

        // generator
        obj = Generators.range();
        Assertions.assertSame(IntGenerator.class, IterableItr.of(obj).getClass());

        obj = (Iterable<Integer>) () -> (Generator<Integer>) () -> 0;
        Assertions.assertInstanceOf(Generator.class, IterableItr.of(obj));

        Object temp = obj;
        Assertions.assertThrows(ClassCastException.class, () -> {
            IterableItr<String> cast = IterableItr.of(temp);
            String s = cast.nextElement();
        });


        // EnumerationItr
        Assertions.assertSame(IterableItr.of(null), NoneItr.of());
        Assertions.assertTrue(IterableItr.of(enumeration()).hasNext());

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

        Assertions.assertTrue(IterableItr.of(new IteratorEnumeration(Arrays.asList(1, 2, 3).iterator())).hasNext());

    }

    Enumeration<Integer> enumeration() {

        Vector<Integer> vector = new Vector<>(1);
        vector.add(1);
        return vector.elements();
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
        Assertions.assertThrows(UnsupportedOperationException.class, () -> IterableItr.ofs(1).remove());
    }

    @Test
    void iterator() {

        Assertions.assertSame(NoneItr.of(), NoneItr.of().iterator());

        IterableItr<Integer> ofs = IterableItr.ofs(1);
        Iterator<Integer> iterator = ofs.iterator();
        iterator.next();

        iterator = ofs.iterator();
        Assertions.assertTrue(iterator.hasNext());


        ofs = IterableItr.of(ofs);
        iterator.next();
        iterator = ofs.iterator();
        Assertions.assertTrue(iterator.hasNext());
        iterator.next();
        Assertions.assertFalse(iterator.hasNext());

    }

    @Test
    void hasMoreElements() {
        Assertions.assertFalse(NoneItr.of().hasMoreElements());
    }

    @Test
    void nextElement() {
        Assertions.assertThrows(NoSuchElementException.class, () -> NoneItr.of().nextElement());
    }

    private static class IteratorEnumeration implements Iterator<Integer>, Enumeration<Integer> {

        final Iterator<Integer> iterator;

        private IteratorEnumeration(Iterator<Integer> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasMoreElements() {
            return hasNext();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Integer next() {
            return iterator.next();
        }

        @Override
        public Integer nextElement() {
            return next();
        }
    }
}
