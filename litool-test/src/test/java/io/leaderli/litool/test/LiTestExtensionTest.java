package io.leaderli.litool.test;

import io.leaderli.litool.core.test.BooleanValues;
import io.leaderli.litool.core.test.IntValues;
import io.leaderli.litool.core.test.ObjectValues;
import io.leaderli.litool.core.util.ConsoleUtil;
import org.junit.jupiter.api.Assertions;

/**
 * @author leaderli
 * @since 2022/8/18 10:46 AM
 */
class LiTestExtensionTest {
    @LiTest
    void test(TestA testA) {
        Assertions.assertNull(testA);
    }

    @LiTest
    void test2(@ObjectValues TestA testA) {
        Assertions.assertNotNull(testA);
    }

    @LiTest
    void test(@IntValues({1, 2}) int age, @IntValues({3, 4}) int size, @BooleanValues boolean have) {


        ConsoleUtil.print(age, size, have);
//        Assertions.assertEquals(1,age);
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
    }


}
