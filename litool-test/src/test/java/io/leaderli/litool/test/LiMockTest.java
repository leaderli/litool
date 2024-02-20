package io.leaderli.litool.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LiMockTest {

    static {
        LiMock.skipClassInitializer(Error.class);
    }

    @LiTest
    public void test() {
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
        Assertions.assertEquals(4, Error.m4());
        LiMock.mockStatic(Error.class, m -> true, (method, args) -> 100);
        Assertions.assertEquals(100, Error.m3());
        Assertions.assertEquals(100, Error.m1());
        Assertions.assertEquals(100, Error.m4());


    }

    @Test
    void test2() {
        System.out.println(Error.m1());
        System.out.println(Error.m3());
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


}
