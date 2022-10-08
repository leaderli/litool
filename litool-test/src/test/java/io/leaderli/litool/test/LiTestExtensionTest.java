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
    @LiTest
    void test(TestA testA) {
        Assertions.assertEquals(0, testA.age);
        Assertions.assertEquals(0, testA.height);
        Assertions.assertFalse(testA.gender);
    }

    @LiTest
    void test2(@ObjectValues TestA testA) {
        Assertions.assertTrue(testA.age >= 1);
    }

    @LiTest
    void test(@IntValues({1, 2}) int age) {

        Assertions.assertTrue(age < 3);
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
