package io.leaderli.litool.core.env;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/9/24 9:08 AM
 */
class OSInfoTest {
    @Test
    void test() {

        Assertions.assertEquals(System.getProperty("os.name").contains("Window"), OSInfo.isWindows());

    }

}
