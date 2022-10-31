package io.leaderli.litool.test;

import io.leaderli.litool.core.exception.AssertException;
import io.leaderli.litool.core.test.*;
import io.leaderli.litool.test2.limock.Foo;
import io.leaderli.litool.test2.limock.GetSetBean;
import io.leaderli.litool.test2.limock.StaticBlock;
import io.leaderli.litool.test2.limock.TestBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

class LiMockTest {

    static {
        LiMock.ignoreTypeInitError(StaticBlock.class);
    }

    static void init() {
        LiMock.mock(TestBean.class);
        TestBean testBean = new TestBean();
        LiMock.light(testBean::m1);
        LiMock.light(testBean::m3);
        LiMock.light(testBean::m4);
        LiMock.whenArgs(() -> testBean.m2(0), params -> {
            int len = (int) params[0];
            if (len == 0) {
                return new Object[]{-100, 0};
            }
            return new Object[]{17, 19};
        });
        LiMock.when(testBean::m5, (Foo) null);

    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    @MockInit
    @LiTest
    void test(@IntValues({0, 1}) int length) {

        Foo foo = LiTestAssert.recording(Foo.class);
        TestBean testBean = new TestBean();
        testBean.m1();
        testBean.m2(1);
        Foo foo1 = testBean.m3();

        Assertions.assertEquals(ArrayList.class, testBean.m4().getClass());

        Assertions.assertNull(testBean.m5());
        Assertions.assertEquals(100, testBean.m6());
        foo1.init(length, length);

        Foo instance = Foo.instance();

        LiTestAssert.assertReturn(() -> foo.init(1, 1), length * 2);
        LiTestAssert.assertArgs(() -> foo.init(1, 1), length, length);
        LiTestAssert.assertCalled(() -> foo.init(1, 1));
        LiTestAssert.assertNotCalled(() -> foo.notCall(1, 1));

        foo1.notCall(0, 0);
        LiTestAssert.assertCalled(() -> foo.notCall(1, 1));

        LiTestAssert.assertion(Foo::instance, (args, ret) -> Assertions.assertSame(instance, ret));
        LiTestAssert.assertion(Foo::instance, (args, ret) -> Assertions.assertSame(instance, ret));
    }


    private static class T1 implements Function<CartesianContext, Object[]> {

        @Override
        public Object[] apply(CartesianContext cartesianContext) {
            return new Object[]{new ArrayList<>(), new GetSetBean()};
        }
    }

    @LiTest
    void test(@ClassValues({Object.class, List.class, GetSetBean.class}) Class<?> type, @FactoryValues(T1.class) Object obj) {
        Assertions.assertDoesNotThrow(() -> LiMock.runGetSet(type, obj));

    }

    @Test
    void test() {

        Assertions.assertThrows(AssertException.class, () -> LiMock.ignoreTypeInitError(LiMockTest.class));
    }

    static void init2(CartesianContext context) {
        context.registerCustomValuable(IntValues.class, (type, annotation, annotatedElement, context1) -> new Object[]{100});
    }

    @MockContext("init2")
    @LiTest
    void test2(@IntValues({1, 2, 3}) int age) {

        Assertions.assertEquals(100, age);
    }

    @LiTest
    void test3(@IntValues({1, 2, 3}) int age) {

        Assertions.assertTrue(age < 4);
    }


    static Object[] values() {
        return new Object[]{1, 2, 3};
    }

    @LiTest
    void test(@DynamicValues("values") Object obj) {

        Assertions.assertTrue(obj instanceof Integer);
    }

    @Test
    void runGetSet() {
        GetSetBean getSetBean = new GetSetBean();
        Assertions.assertSame(0, getSetBean.getAge());
        LiMock.runGetSet(GetSetBean.class, getSetBean);
        Assertions.assertSame(1, getSetBean.getAge());

    }

    @MockInit("init3")
    @LiTest
    void staticBlock3() {
        Assertions.assertEquals(StaticBlock.size(), StaticBlock.size);
    }

    @SuppressWarnings("all")
    static void init3() {
        LiMock.mock(StaticBlock.class);

    }

    @MockInit("init4")
    @LiTest
    void staticBlock4() {
        Assertions.assertEquals(100, StaticBlock.size);

    }

    static void init4() {
        LiMock.mock(StaticBlock.class);
        LiMock.when(StaticBlock::size, 300);

    }


    static class Inter {
        int age;
        Supplier<Inter> supplier;


        public boolean getAge() {

            return false;
        }
    }


    public static void inter() {
        LiMock.mock(Inter.class);
        LiMock.light(() -> new Inter().supplier.get());
        LiMock.light(() -> new Inter().getAge());

    }

    @MockInit("inter")
    @LiTest
    void testInter() {
        Inter inter = new Inter();
        Assertions.assertFalse(inter.getAge());
        Assertions.assertNotNull(inter.supplier.get());
    }

}

