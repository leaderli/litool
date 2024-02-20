package io.leaderli.litool.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LiMockTest {

    static {
        LiMock.skipClassInitializer(Error.class);
    }


    @LiTest
    public void test() {
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
    void testGetInvoke() {
        MockMethodInvoker.invokers.clear();
        LiMock.mockStatic(Error.class, m -> true, (method, args) -> 100);
        Assertions.assertEquals(4, MockMethodInvoker.invokers.size());
    }

    @Test
    void test3() {

        LiMock.builder(Error.class).when(Error.m1()).then(101).build();
        Assertions.assertEquals(101, Error.m1());
        Assertions.assertEquals(4, Error.m4());
        LiMock.builder(Error.class).when(Error.m1(1)).then(11)
                .other(() -> 12).build();
        Assertions.assertEquals(11, Error.m1(1));
        Assertions.assertEquals(12, Error.m1(2));
        LiMock.builder(Error.class).when(Error.m1(1)).then(11)
                .other(12).build();
        Assertions.assertEquals(11, Error.m1(1));
        Assertions.assertEquals(12, Error.m1(2));

        LiMock.builder(Error.class).when(Error.m1(1)).other((m, args) -> {
            if ((int) args[0] == 1) {
                return 21;
            }
            return 22;
        }).build();
        Assertions.assertEquals(21, Error.m1(1));
        Assertions.assertEquals(22, Error.m1(2));
        Bean bean = new Bean();

        LiMock.builder(Bean.class).when(bean.m1(1)).then(11)
                .other(12).build();
        Assertions.assertEquals(11, bean.m1(1));
        Assertions.assertEquals(12, bean.m1(2));

    }

    static class Error {
        static {
            if (1 == 1) {
                throw new IllegalStateException();
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
}
