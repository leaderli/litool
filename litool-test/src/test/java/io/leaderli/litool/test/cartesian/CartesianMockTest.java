package io.leaderli.litool.test.cartesian;

import io.leaderli.litool.test2.limock.Foo;
import io.leaderli.litool.test2.limock.GetSetBean;
import io.leaderli.litool.test2.limock.StaticBlock;
import io.leaderli.litool.test2.limock.TestBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

class CartesianMockTest {


    static {
        CartesianMock.skipClassInitializerError(StaticBlock.class);
    }

    static void init() {
        CartesianMock.mock(TestBean.class);
        TestBean testBean = new TestBean();
        CartesianMock.light(testBean::m1);
        CartesianMock.light(testBean::m3);
        CartesianMock.light(testBean::m4);


        MethodResultCartesianForParameter methodResultCartesianForParameter = new MethodResultCartesianForParameter(-100, 0);


        CartesianMock.whenArgs(() -> testBean.m2(0), methodResultCartesianForParameter);
        CartesianMock.when(testBean::m5, (Foo) null);

    }

    static void init1() {
        CartesianMock.mock(StaticBlock.class);
        CartesianMock.when(StaticBlock::size, 300);
    }


    private static class T1 implements Function<CartesianContext, Object[]> {

        @Override
        public Object[] apply(CartesianContext cartesianContext) {
            return new Object[]{new ArrayList<>(), new GetSetBean()};
        }
    }

    public static void inter() {
        CartesianMock.mock(Inter.class);
        CartesianMock.light(() -> new Inter().supplier.get());
        CartesianMock.light(() -> new Inter().getAge());

    }


    static void init2(CartesianContext context) {
        context.registerCustomValuable(IntValues.class, (type, annotation, annotatedElement, context1) -> new Object[]{100});
    }

    @MockContext("init2")
    @CartesianTest
    void test2(@IntValues({1, 2, 3}) int age) {

        Assertions.assertEquals(100, age);
    }

    @CartesianTest
    void test3(@IntValues({1, 2, 3}) int age) {

        Assertions.assertTrue(age < 4);
    }


    static Object[] values() {
        return new Object[]{1, 2, 3};
    }

    @CartesianTest
    void test(@DynamicValues("values") Object obj) {

        Assertions.assertInstanceOf(Integer.class, obj);
    }

    static void mockMap() {
        CartesianMock.mock(Bar.class);
        CartesianMock.light(() -> new Bar().getMap());
        CartesianMock.mock(MockMap.class);

        Map<String, Integer> map = new MockMap<>();

        CartesianMock.when(() -> map.get(""), 1, 2);

    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @MockInit
    @CartesianTest
    void test(@IntValues({0, 1}) int length) {

        Foo foo = CartesianTestAssert.recording(Foo.class);
        TestBean testBean = new TestBean();
        testBean.m1();
        testBean.m2(1);
        Foo foo1 = testBean.m3();

        Assertions.assertEquals(MockList.class, testBean.m4().getClass());

        Assertions.assertNull(testBean.m5());
        Assertions.assertEquals(100, testBean.m6());
        foo1.init(length, length);

        Foo instance = Foo.instance();

        CartesianTestAssert.assertReturn(() -> foo.init(1, 1), length * 2);
        CartesianTestAssert.assertArgs(() -> foo.init(1, 1), length, length);
        CartesianTestAssert.assertCalled(() -> foo.init(1, 1));
        CartesianTestAssert.assertNotCalled(() -> foo.notCall(1, 1));

        foo1.notCall(0, 0);
        CartesianTestAssert.assertCalled(() -> foo.notCall(1, 1));

        CartesianTestAssert.assertion(Foo::instance, (args, ret) -> Assertions.assertSame(instance, ret));
        CartesianTestAssert.assertion(Foo::instance, (args, ret) -> Assertions.assertSame(instance, ret));
    }

    @MockInit("init1")
    @CartesianTest
    void staticBlock1() {
        Assertions.assertEquals(100, StaticBlock.size);
        Assertions.assertEquals(300, StaticBlock.size());

    }



    static class Inter {
        int age;
        Supplier<Inter> supplier;


        public boolean getAge() {

            return true;
        }
    }

    @CartesianTest
    void test(@ClassValues({Object.class, List.class, GetSetBean.class}) Class<?> type, @FactoryValues(T1.class) Object obj) {
        Assertions.assertDoesNotThrow(() -> CartesianMock.runGetSet(type, obj));

    }

    @MockInit("inter")
    @CartesianTest
    void testInter() {
        Inter inter = new Inter();
        Assertions.assertFalse(inter.getAge());
        Assertions.assertNotNull(inter.supplier.get());
    }

    @Test
    void runGetSet() {
        GetSetBean getSetBean = new GetSetBean();
        Assertions.assertSame(0, getSetBean.getAge());
        CartesianMock.runGetSet(GetSetBean.class, getSetBean);
        Assertions.assertSame(1, getSetBean.getAge());

    }

    @MockInit("mockMap")
    @CartesianTest
    void testMockMap() {

        Bar bar = new Bar();
        Assertions.assertTrue(bar.getMap().get("abc") > 0);


    }

    static class Bar {

        private Map<String, Integer> map;

        public Map<String, Integer> getMap() {
            return map;
        }

        public void setMap(Map<String, Integer> map) {
            this.map = map;
        }
    }
}

