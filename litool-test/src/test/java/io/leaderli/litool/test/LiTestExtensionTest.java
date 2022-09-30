package io.leaderli.litool.test;

import io.leaderli.litool.core.test.BooleanValues;
import io.leaderli.litool.core.test.IntValues;
import io.leaderli.litool.core.test.ObjectValues;
import org.junit.jupiter.api.Assertions;

/**
 * @author leaderli
 * @since 2022/8/18 10:46 AM
 */
class LiTestExtensionTest {
    //    @LiTest
    void test(TestA testA) {
        Assertions.assertNull(testA);
    }

    //    @LiTest
    void test2(@ObjectValues TestA testA) {
        Assertions.assertNotNull(testA);
    }

    //    @LiTest
    void test(@IntValues({1, 2}) int age, @IntValues({3, 4}) int size, @BooleanValues boolean have) {


        Assertions.assertTrue(age < 3);
    }

    //    @LiTest
    void test3(@ObjectValues TestB testb) {
        Assertions.assertNotNull(testb);
    }

    //    @LiTest
    void test4(@ObjectValues TestA testA) {

        new LiTestAssert<>((TestMatch<TestA>) testA1 -> true).test(testA);
    }

    private static class TestB {
        @ObjectValues
        TestA testA;

        @IntValues({0, 1})
        private int state;

        @Override
        public String toString() {
            return "TestB{" +
                    "testA=" + testA +
                    ", state=" + state +
                    '}';
        }
    }

    private static class TestA {

        @IntValues({1, 2})
        private int age;
        @IntValues({180, 173})
        private int height;

        @BooleanValues
        private boolean gender;


        public void have(@IntValues({10, 20}) int age) {

        }

        @Override
        public String toString() {
            return "TestA{" +
                    "age=" + age +
                    ", height=" + height +
                    ", gender=" + gender +
                    '}';
        }
    }


}
