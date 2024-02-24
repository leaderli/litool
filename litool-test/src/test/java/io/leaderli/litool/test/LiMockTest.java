package io.leaderli.litool.test;

import io.leaderli.litool.core.meta.LiBox;
import io.leaderli.litool.core.type.ModifierUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LiMockTest {

    static {
        LiMock.skipClassInitializer(Error.class);
    }


    @LiTest
    void skipClassConstructors() {

        Assertions.assertEquals(2, new Foo().a);
        LiMock.skipClassConstructors(Foo.class);
        Assertions.assertEquals(0, new Foo().a);

        Assertions.assertEquals(2, new Foo2(2).a);
        LiMock.skipClassConstructors(Foo2.class);
        Assertions.assertEquals(0, new Foo2(2).a);


    }


    @LiTest
    void skipClassConstructors2() {
//        Assertions.assertEquals(2, new Foo3(2).a);
        LiMock.skipClassConstructors(Foo3.class);
        Assertions.assertEquals(0, new Foo3(2).a);
        LiMock.skipClassConstructors(Foo5.class);
        Assertions.assertEquals(0, new Foo5(5, "5").a);

        Assertions.assertEquals(2, new Foo7(1, 1).a);
        LiMock.skipClassConstructors(Foo7.class);
        Assertions.assertEquals(0, new Foo7(1, 1).a);
    }

    @LiTest
    public void testMockStatic() {

        LiMock.mockStatic(Error.class, m -> true, (method, args) -> 100);
        Assertions.assertEquals(100, Error.m3());
        Assertions.assertEquals(100, Error.m1());
        Assertions.assertEquals(100, Error.m4());
        LiMock.mockStatic(Error.class, method -> method.getName().equals("m1"),
                (method, args) -> {
                    if (args.length == 0) {
                        return 30;
                    }
                    return (int) args[0] + 40;
                }
        );
        Assertions.assertEquals(30, Error.m1());
        Assertions.assertEquals(44, Error.m1(4));
        // 重新代理后，m4 应该不被代理
        Assertions.assertEquals(4, Error.m4());
        Assertions.assertNotEquals(100, Error.m4());


    }

    @Test
    void testMockReset() {
        LiMock.mockStatic(Error.class, m -> "m1".equals(m.getName()), (method, args) -> 100, false);
        LiMock.mockStatic(Error.class, m -> "m4".equals(m.getName()), (method, args) -> 200, false);
        Assertions.assertEquals(100, Error.m1());
        Assertions.assertEquals(200, Error.m4());
        LiMock.mockStatic(Error.class, m -> "m4".equals(m.getName()), (method, args) -> 200, true);
        Assertions.assertEquals(1, Error.m1());
        Assertions.assertEquals(200, Error.m4());

    }

    @LiTest
    void testGetInvoke() {
        MockMethodInvoker.invokers.clear();
        LiMock.mockStatic(Error.class, m -> true, (method, args) -> 100);
        Assertions.assertEquals(4, MockMethodInvoker.invokers.size());
    }

    @LiTest
    void testMockBean() {

        LiMock.mocker(Error.class).when(Error.m1(), 101).build();
        Assertions.assertEquals(101, Error.m1());
        Assertions.assertEquals(4, Error.m4());
        LiMock.mocker(Error.class).when(Error.m1(1), 11, (m, args) -> 12).build();
        Assertions.assertEquals(11, Error.m1(1));
        Assertions.assertEquals(12, Error.m1(2));
        LiMock.mocker(Error.class).when(Error.m1(1), 11, 12).build();
        Assertions.assertEquals(11, Error.m1(1));
        Assertions.assertEquals(12, Error.m1(2));

        LiMock.mocker(Error.class).when(Error.m1(1), (m, args) -> {
            if ((int) args[0] == 1) {
                return 21;
            }
            return 22;
        }).build();
        Assertions.assertEquals(21, Error.m1(1));
        Assertions.assertEquals(22, Error.m1(2));
        Bean bean = new Bean();


    }

    @LiTest
    void testWhen() {
        LiMock.mocker(Error.class).when(Error.m1(1), 11, 12).build();
        Assertions.assertEquals(11, Error.m1(1));
        Assertions.assertEquals(12, Error.m1(2));
        LiMock.mocker(Error.class).when(Error.m1(1), 11).build();
        Assertions.assertEquals(11, Error.m1(1));
        Assertions.assertEquals(3, Error.m1(2));

        LiMock.mocker(Error.class).when(Error.m1(1), 10)
                .when(Error.m1(), 10)
                .build();
        Assertions.assertEquals(10, Error.m1(1));
        Assertions.assertEquals(3, Error.m1(2));
        Assertions.assertEquals(10, Error.m1());
        LiMock.mocker(Error.class).when(Error.m1(1), 20).build();
        Assertions.assertEquals(20, Error.m1(1));
        Assertions.assertEquals(1, Error.m1());
        Assertions.assertEquals(3, Error.m1(2));
        LiMock.reset();
        LiMock.mocker(Error.class).when(Error.m1(1), 20).build();
        Assertions.assertEquals(20, Error.m1(1));
        Assertions.assertEquals(1, Error.m1());
        Assertions.assertEquals(3, Error.m1(2));


        Foo1 foo1 = () -> "";
        LiMock.mocker(Foo1.class).when(foo1.get2(), "123").build();
        Assertions.assertEquals("123", foo1.get2());

        Assertions.assertThrows(IllegalStateException.class, () -> LiMock.mocker(Foo1.class).when(foo1.get(), "123").build());

    }

    @LiTest
    void testSkipClassInitializer() {
        Assertions.assertDoesNotThrow(() -> Error.m1());
    }


    @LiTest
    void testRecordStatic() {

        Assertions.assertEquals(2, Error.m1(1));
        LiMock.mocker(Error.class).when(Error.m1(1), 11, 12).build();
        Assertions.assertEquals(11, Error.m1(1));
        Assertions.assertEquals(12, Error.m1(2));

        LiMock.recordStatic(Error.class, ModifierUtil::isPublic, (obj, args, value) -> {
            if (args.length == 1) {
                if ((int) args[0] == 1) {
                    Assertions.assertEquals(11, value);
                } else {
                    Assertions.assertEquals(12, value);
                }
            }
            return null;
        });
//        Error.m1(1);
//        Error.m1(2);
    }

    @Test
    void testRecord() {

        LiBox<Object> box = LiBox.none();
        LiMock.record(Bean.class, m -> "m1".equals(m.getName()), (_this, args, _return) -> {
            box.value(_this);
            return null;
        });

        Bean bean = new Bean();
        bean.m1();
        Assertions.assertSame(bean, box.value());

    }

    @Test
    void testStr() {

        Str1 str1 = MockBean.mockBean(Str1.class);
        Assertions.assertEquals("a", Str1.a);
        Assertions.assertEquals("", str1.b);

    }

    @SuppressWarnings("ConstantValue")
    static class Error {
        static {
            if (1 == 1) {
                throw new IllegalStateException("1 == 1");
            }
        }

        public static int m1() {
            return 1;
        }

        public static int m1(int a) {
            return a + 1;
        }

        public static int m3() {
            return m4();
        }

        private static int m4() {
            return 4;
        }


    }

    static class Bean {
        public int m1() {
            return 1;
        }

        public int m1(int a) {
            return a + 1;
        }

        public int m3() {
            return m4();
        }

        private int m4() {
            return 4;
        }
    }

    @SuppressWarnings("UnusedAssignment")
    static class Foo {
        public int a = 1;

        public Foo() {
            a = 2;
        }
    }

    @SuppressWarnings("UnusedAssignment")
    static class Foo2 {
        public int a = 1;

        public Foo2(int a) {
            this.a = a;
        }
    }

    static class Foo3 extends Foo2 {

        public Foo3(int a) {
            super(a);
        }
    }

    static class Foo4 {
        public int a;

        public Foo4(int a, String b) {
        }
    }

    static class Foo5 extends Foo4 {


        public Foo5(int a, String b) {
            super(a, b);
        }
    }

    static class Foo6 {
        int a;

        Foo6(int a) {
            this.a = 1;
        }

        Foo6() {
            this.a = 0;
        }

        Foo6(int a, int b) {
            this.a = 2;
        }
    }

    static class Foo7 extends Foo6 {
        public Foo7(int a, int b) {
            super(a, b);
        }
    }

    interface Foo1 {
        Object get();

        default Object get2() {
            return get();
        }
    }

    static class Str1 {
        public static String a = "a";
        public String b;
    }
}
