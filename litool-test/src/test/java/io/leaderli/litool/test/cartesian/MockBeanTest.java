package io.leaderli.litool.test.cartesian;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.leaderli.litool.test.MockBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

class MockBeanTest {

    Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    @Test
    void test() {

        FooBar fooBar = MockBean.instance(FooBar.class).create();

        Assertions.assertSame(int[].class, fooBar.getArr().getClass());
        Assertions.assertSame(Integer[].class, fooBar.getWrapper().getClass());
        Assertions.assertSame(MockList.class, fooBar.getList().getClass());

        Assertions.assertNotNull(fooBar.getBar());
        Assertions.assertNotNull(fooBar.getFoo());

        Map<Integer, List<Integer>> map = fooBar.getMap();

        Assertions.assertEquals(MockMap.class, map.getClass());


    }

    @Test
    void mockBean() {

        Assertions.assertNotNull(MockBean.mockBean(Consumer[].class));
        Assertions.assertNotNull(MockBean.mockBean(List.class));
        Assertions.assertNotNull((MockBean.mockBean(Integer.class)));
        Assertions.assertNotNull((MockBean.mockBean(int.class)));
        Assertions.assertNull(MockBean.mockBean(Consumer.class));


        Assertions.assertEquals(MockMap.class, MockBean.mockBean(Map.class).getClass());
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
}
