package io.leaderli.litool.core.type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.leaderli.litool.core.lang.lean.LeanKey;
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
    public void testMockBean() {

        Str1 str1 = BeanCreator.mockBean(Str1.class);
        Assertions.assertEquals("a", Str1.a);
        Assertions.assertEquals("", str1.b);

    }

    @SuppressWarnings("rawtypes")
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

        Bar bar = BeanCreator.mockBean(Bar.class);

        Assertions.assertSame(bar, bar.getBar());
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

        BeanCreator.MockBeanBuilder<AbstractFoo> beanCreatorBuild = BeanCreator.create(AbstractFoo.class).cache(AbstractFoo.class, new AbstractFoo() {
            @Override
            public void m1() {

            }
        });
        Assertions.assertNotNull(beanCreatorBuild.build().create());

        Assertions.assertNotNull(beanCreatorBuild.type(Foo3.class).build().create().foo);

        Foo2 foo2 = BeanCreator.create(Foo2.class).populate("b", (b, f, t) -> {
            if (f.getName().equals("b")) {
                return "b";
            }
            return null;
        }).build().create();
        Assertions.assertEquals("", foo2.a);
        Assertions.assertEquals("b", foo2.b);
        foo2 = BeanCreator.create(Foo2.class).populate("a", 1).build().create();
        Assertions.assertEquals("", foo2.a);
        foo2 = BeanCreator.create(Foo2.class)
                .populate("a", "1")
                .populate("b", 1)
                .build().create();
        Assertions.assertEquals("1", foo2.a);
        Assertions.assertEquals("", foo2.b);

        Annotated1 annotated1 = BeanCreator.create(Annotated1.class).annotated(LeanKey.class, (bean, field, fieldType) -> "leanKey").build().create();
        Assertions.assertEquals("leanKey", annotated1.b);
        foo2 = BeanCreator.create(Foo2.class).populate(int.class, 1).build().create();
        Assertions.assertEquals("", foo2.a);
        Assertions.assertEquals("", foo2.b);
        Assertions.assertEquals(1, foo2.c);

        foo2 = BeanCreator.create(Foo2.class).populate((bean, field, fieldType) -> {
            if (fieldType == Integer.class) {
                return 10;
            }
            return null;
        }).build().create();
        Assertions.assertEquals("", foo2.a);
        Assertions.assertEquals("", foo2.b);
        Assertions.assertEquals(10, foo2.c);

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

    static class Foo3 {
        AbstractFoo foo;
    }

    static class Foo2 {
        private String a;
        private String b;
        private Integer c;
    }

    static class MockMap<K, V> extends HashMap<K, V> {

    }

    static class Str1 {
        public static String a = "a";
        public String b;
    }

    static class Annotated1 {
        @LeanKey("123")
        public String b;
    }
}
