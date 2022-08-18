package io.leaderli.litool.test;

import io.leaderli.litool.core.util.ConsoleUtil;
import org.junit.jupiter.api.Assertions;

/**
 * @author leaderli
 * @since 2022/8/18 10:46 AM
 */
class LiTestExtensionTest {


    @LiTest
    void test() {
        System.out.println("test");

    }
    @LiTest
    void test(int age,int size) {

        ConsoleUtil.print(age,size);
        Assertions.assertEquals(1,age);
    }



}
