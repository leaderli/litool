package io.leaderli.litool.core.util;

import java.util.Arrays;

/**
 * @author leaderli
 * @since 2022/6/25
 */
class LiPrintUtilTest {


    //    @Test
    public void print() {

        ConsoleUtil.print0("123", 4565);
        ConsoleUtil.print("123", "456");
        ConsoleUtil.print(1, null, 1, Arrays.asList(1, null));
        ConsoleUtil.print(null, null, 1, Arrays.asList(1, null));

        ConsoleUtil.print_format("a {0}", 2, 3);


        ConsoleUtil.print0("_", "1", "2");


        ConsoleUtil.print(1, 2, 3);
        ConsoleUtil.println(1, 2, 3);
        ConsoleUtil.println(Arrays.asList(1, 2, 3));


    }
}
