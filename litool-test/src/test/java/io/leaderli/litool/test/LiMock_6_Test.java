package io.leaderli.litool.test;

import io.leaderli.litool.core.type.MethodFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 测试recorder的回收
 */
class LiMock_6_Test {


    @LiTest
    void testRecordCall() {
        LiMock.record(Error6.class, MethodFilter.name("m1"), (method, args, originReturn) -> Assertions.assertEquals(1, args[0]));
        Error6.m1(1);
    }

    @LiTest
    void testRecordCall2() {
        Error6.m1(2);
    }


    static {
        LiMock.skipClassConstructors(So.class);
    }

    @Test
    void test() {
        System.out.println(new So().m1().age);
        LiMock.mock(So.class, MethodFilter.name("m1"), MethodProxy.of(new Error6(10)));
        System.out.println(new So().m1().age);

    }

    static class Error6 {
        public int age;

        public Error6() {
        }

        public Error6(int age) {
            this.age = age;
        }

        public static int m1(int i) {
            return 1;
        }
    }

    static class Fa<T> {

        public T m1() {
            System.out.println("Fa");
            return null;
        }
    }

    static class So extends Fa<Error6> {
        @Override
        public Error6 m1() {
            super.m1();
            System.out.println("son");
            return new Error6();
        }
    }
}
