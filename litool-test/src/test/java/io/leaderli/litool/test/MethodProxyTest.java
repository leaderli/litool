package io.leaderli.litool.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

class MethodProxyTest {

    MethodProxyTest() throws NoSuchMethodException {
    }

    @SuppressWarnings("rawtypes")
    @Test
    void test() throws Throwable {

        MethodProxy<?> quick = MethodProxy.quick(1, 2, 3);

        Assertions.assertArrayEquals(new Integer[]{1, 2, 3}, (Object[]) quick.apply(m1, null));
        Assertions.assertArrayEquals(new int[]{1, 2, 3}, (int[]) quick.apply(m2, null));
        List list = (List) quick.apply(m3, null);
        Assertions.assertEquals(3, list.size());
        Map map = (Map) quick.apply(m4, null);
        Assertions.assertEquals(1, map.size());

        Bean bean = (Bean) quick.apply(m5, null);
        Assertions.assertEquals(2, bean.age);
        Assertions.assertEquals(3, bean.gender);
    }

    Method m1 = MethodProxyTest.class.getMethod("m1");
    Method m2 = MethodProxyTest.class.getMethod("m2");
    Method m3 = MethodProxyTest.class.getMethod("m3");
    Method m4 = MethodProxyTest.class.getMethod("m4");
    Method m5 = MethodProxyTest.class.getMethod("m5");

    public Integer[] m1() {
        return null;
    }

    public int[] m2() {
        return null;
    }

    public List<Integer> m3() {
        return null;
    }

    public Map<String, Integer> m4() {
        return null;
    }

    public Bean m5() {
        return null;
    }

    private static class Bean {
        private final String name = "0";
        /**
         * 测试只有get
         */
        private int age;
        /**
         * 测试只有set
         */
        private Integer gender;
    }
}
