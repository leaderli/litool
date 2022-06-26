package io.leaderli.litool.core.util;

import io.leaderli.litool.core.meta.LiBox;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author leaderli
 * @since 2022/6/25
 */
class LiPrintUtilTest {


    @Test
    public void print() {

        LiPrintUtil.print("123", 4565);
        LiPrintUtil.print("123", "456");
        LiPrintUtil.print(1,null,1,Arrays.asList(1,null));
        LiPrintUtil.print((LiBox<String>) null,null,1,Arrays.asList(1,null));

        LiPrintUtil.print_format("a {0}",2,3);







    }
}
