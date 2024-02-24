package io.leaderli.litool.core.type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.leaderli.litool.test.cartesian.IntValues;
import io.leaderli.litool.test.cartesian.MockMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

class BeanCreatorTest {

    Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    @Test
    void test() {

        FooBar fooBar = BeanCreator.mockBean(FooBar.class);
        Assertions.assertSame(int[].class, fooBar.getArr().getClass());
        Assertions.assertSame(Integer[].class, fooBar.getWrapper().getClass());
        Assertions.assertSame(ArrayList.class, fooBar.getList().getClass());

        Assertions.assertNotNull(fooBar.getBar());
        Assertions.assertNotNull(fooBar.getFoo());

        Map<Integer, List<Integer>> map = fooBar.getMap();

        Assertions.assertEquals(HashMap.class, map.getClass());


    }

    @Test
    void mockBean() {

        Assertions.assertNotNull(BeanCreator.mockBean(Consumer[].class));
        Assertions.assertNotNull(BeanCreator.mockBean(List.class));
        Assertions.assertNotNull((BeanCreator.mockBean(Integer.class)));
        Assertions.assertNotNull((BeanCreator.mockBean(int.class)));
        Assertions.assertNull(BeanCreator.mockBean(Consumer.class));


        Assertions.assertEquals(HashMap.class, BeanCreator.mockBean(Map.class).getClass());

        Assertions.assertEquals(MockMap.class, BeanCreator.create(Map.class).head(MockMap.class, t -> new MockMap<>()).build().create().getClass());

        Assertions.assertNotNull(
                BeanCreator.create(AbstractFoo.class).cache(AbstractFoo.class, new AbstractFoo() {
                    @Override
                    public void m1() {

                    }
                }).build().create());

        Foo2 foo2 = BeanCreator.create(Foo2.class).populate((b, f, t) -> {
            if (f.getName().equals("b")) {
                return "b";
            }
            return null;
        }).build().create();
        Assertions.assertEquals("", foo2.a);
        Assertions.assertEquals("b", foo2.b);
    }

    static abstract class Foo<T, R> {

        private T t;
        private R r;
        private Map<T, R> map;

        public T getT() {
            return t;
        }

        public void setT(T t) {
            this.t = t;
        }

        public R getR() {
            return r;
        }

        public void setR(R r) {
            this.r = r;
        }

        public Map<T, R> getMap() {
            return map;
        }

        public void setMap(Map<T, R> map) {
            this.map = map;
        }
    }

    static class Bar<T extends Number, R extends List<T>> extends Foo<T, R> {
        private int size;
        private int[] arr;
        private T[] wrapper;
        private List<T> list;
        private Bar<T, R> bar;
        private Foo<T, R> foo;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int[] getArr() {
            return arr;
        }

        public void setArr(int[] arr) {
            this.arr = arr;
        }

        public T[] getWrapper() {
            return wrapper;
        }

        public void setWrapper(T[] wrapper) {
            this.wrapper = wrapper;
        }

        public List<T> getList() {
            return list;
        }

        public void setList(List<T> list) {
            this.list = list;
        }

        public Bar<T, R> getBar() {
            return bar;
        }

        public void setBar(Bar<T, R> bar) {
            this.bar = bar;
        }

        public Foo<T, R> getFoo() {
            return foo;
        }

        public void setFoo(Foo<T, R> foo) {
            this.foo = foo;
        }
    }

    static class FooBar extends Bar<Integer, List<Integer>> {
        @IntValues({1, 2})
        private boolean init;

        public boolean isInit() {
            return init;
        }

        public void setInit(boolean init) {
            this.init = init;
        }
    }

    abstract static class AbstractFoo {
        public abstract void m1();
    }

    static class Foo2 {
        private String a;
        private String b;
    }
}
