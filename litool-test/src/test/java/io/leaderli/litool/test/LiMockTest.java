package io.leaderli.litool.test;

import io.leaderli.litool.core.test.CartesianContext;
import io.leaderli.litool.core.test.ClassValues;
import io.leaderli.litool.core.test.FactoryValues;
import io.leaderli.litool.core.test.IntValues;
import io.leaderli.litool.test.limock.Foo;
import io.leaderli.litool.test.limock.GetSetBean;
import io.leaderli.litool.test.limock.TestBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

class LiMockTest {


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
    void runGetSet() {
        GetSetBean getSetBean = new GetSetBean();
        Assertions.assertSame(0, getSetBean.getAge());
        LiMock.runGetSet(GetSetBean.class, getSetBean);
        Assertions.assertSame(1, getSetBean.getAge());

    }
}

