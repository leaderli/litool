package io.leaderli.litool.core.lang;

/**
 * @author leaderli
 * @since 2022/9/22 9:24 AM
 */
class ShellTest {

    //    @Test
    void test() {

        System.out.println(System.getProperty("user.dir"));


        new Shell().command("sh", "-c", "echo $PWD");

        String a;

    }


}
