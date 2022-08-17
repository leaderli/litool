package io.leaderli.litool.test;

import io.leaderli.litool.core.util.ConsoleUtil;

/**
 * @author leaderli
 * @since 2022/8/17
 */
public class LiTestTest {
    void test1(int age) {

        System.out.println(age + " 1");
    }

    void test(int age, int size) {

        ConsoleUtil.print(age, size, "2");
    }


    @LiTest
    void test(int age, int size, int l) {

        ConsoleUtil.print(age, size, l, "3");
    }
}
