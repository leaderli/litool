package io.leaderli.litool.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/6/27
 */
class LiNetUtilTest {

    @Test
    void pingable() {
        Assertions.assertTrue(LiNetUtil.pingable("127.0.0.1"));
        Assertions.assertFalse(LiNetUtil.pingable("182.180.333.444"));
        Assertions.assertTrue(LiNetUtil.pingable(""));
        Assertions.assertFalse(LiNetUtil.pingable(null));
    }
}
