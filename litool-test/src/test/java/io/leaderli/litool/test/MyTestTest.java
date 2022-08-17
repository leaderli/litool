package io.leaderli.litool.test;

import io.leaderli.litool.core.util.ConsoleUtil;

/**
 * @author leaderli
 * @since 2022/8/17
 */
public class MyTestTest {


    @MyTest
    void test(int age,int size) {

        ConsoleUtil.print(age,size);
    }
}
