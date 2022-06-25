package io.leaderli.litool.core.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author leaderli
 * @since 2022/6/25
 */
class LiPrintUtilTest {


    @Test
    public void print() {

        System.out.println(null+"");
        LiPrintUtil.print("123", 4565);
        LiPrintUtil.print("123", "456");
        LiPrintUtil.print(1,null,1,Arrays.asList(1,null));

        LiPrintUtil.print_format("a {0}",2,3);




    }
}
