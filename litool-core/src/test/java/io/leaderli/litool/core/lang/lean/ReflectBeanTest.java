package io.leaderli.litool.core.lang.lean;

import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class ReflectBeanTest {

    Gson gson = new Gson();

    @Test
    void test() {

        FooBar fooBar = ReflectBean.instance(FooBar.class).create();


//        System.out.println(Arrays.toString(FooBar.class.getFields()));
//        System.out.println(Arrays.toString(FooBar.class.getDeclaredFields()));


        Assertions.assertSame(int[].class, fooBar.getArr().getClass());
        Assertions.assertSame(Integer[].class, fooBar.getWrapper().getClass());
        Assertions.assertSame(ArrayList.class, fooBar.getList().getClass());

        System.out.println(gson.toJson(fooBar));

    }

    static class Foo<T, R> {

        private T t;
        private R r;

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
    }

    static class Bar<T extends Number, R extends List<T>> extends Foo<T, R> {
        private int size;
        private int[] arr;
        private T[] wrapper;
        private List<T> list;
        private Bar<T, R> bar;

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
    }

    static class FooBar extends Bar<Integer, List<Integer>> {
        private boolean init;

        public boolean isInit() {
            return init;
        }

        public void setInit(boolean init) {
            this.init = init;
        }
    }
}
