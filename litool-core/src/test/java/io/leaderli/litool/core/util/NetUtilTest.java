package io.leaderli.litool.core.util;

import io.leaderli.litool.core.meta.LiConstant;
import io.leaderli.litool.core.net.NetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/6/27
 */
class NetUtilTest {

    @Test
    void pingable() {
        LiConstant.WHEN_THROW = null;
        Assertions.assertTrue(NetUtil.pingable("127.0.0.1"));
        Assertions.assertFalse(NetUtil.pingable("182.180.333.444"));
        Assertions.assertTrue(NetUtil.pingable(""));
        Assertions.assertFalse(NetUtil.pingable(null));
    }
}
