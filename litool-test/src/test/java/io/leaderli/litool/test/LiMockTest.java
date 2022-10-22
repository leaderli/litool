package io.leaderli.litool.test;

import io.leaderli.litool.core.test.IntValues;
import io.leaderli.litool.test.limock.Foo;
import io.leaderli.litool.test.limock.MockBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class LiMockTest {

    @Test
    void mockBean() {

        Assertions.assertNotNull(LiMock.mockBean(Consumer[].class));
        Assertions.assertNotNull(LiMock.mockBean(List.class));
        Assertions.assertNotNull((LiMock.mockBean(Integer.class)));
        Assertions.assertNull(LiMock.mockBean(Consumer.class));

    }


    static void init() {
        LiMock.mock(MockBean.class);
        MockBean mockBean = new MockBean();
        LiMock.light(mockBean::m1);
        LiMock.light(mockBean::m3);
        LiMock.light(mockBean::m4);
        LiMock.whenArgs(() -> mockBean.m2(0), params -> {
            int len = (int) params[0];
            if (len == 0) {
                return new Object[]{-100, 0};
            }
            return new Object[]{17, 19};
        });
        LiMock.when(mockBean::m5, (Foo) null);

    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    @MockInit
    @LiTest
    void test(@IntValues({0, 1}) int length) {

        Foo foo = LiTestAssert.recording(Foo.class);
        MockBean mockBean = new MockBean();
        mockBean.m1();
        mockBean.m2(1);
        Foo foo1 = mockBean.m3();
        Assertions.assertEquals(ArrayList.class, mockBean.m4().getClass());

        Assertions.assertNull(mockBean.m5());
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


}

