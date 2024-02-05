package io.leaderli.litool;

import io.leaderli.litool.test.assit.SimpleTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AssitTest {

    public static String test() {
        return "b";
    }


    @Test
    void test1() {


        Assertions.assertEquals("a", A.test());
        SimpleTest.transfer(A.class, AssitTest.class);
        Assertions.assertEquals("b", A.test());

        SimpleTest.transfer(B.class, A.class);
        Assertions.assertEquals("b", B.test());
    }

    static class A {


        public static String test() {
            return "a";
        }
    }

    static class B {

        static {
            //noinspection ConstantValue
            if (true) {
                throw new RuntimeException("b");
            }
        }

        public static String test() {
            return "a";
        }
    }
}
