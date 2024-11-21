package io.leaderli.litool.core.util;

import io.leaderli.litool.core.env.OSInfo;
import io.leaderli.litool.core.meta.WhenThrowBehavior;
import io.leaderli.litool.core.resource.net.NetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/6/27
 */
class NetUtilTest {

    @Test
    void pingable() {
        if (OSInfo.isWindows()) {
            return;
        }
        WhenThrowBehavior.setIgnore();
        Assertions.assertTrue(NetUtil.pingable("127.0.0.1"));
        Assertions.assertFalse(NetUtil.pingable("182.180.333.444"));
        Assertions.assertTrue(NetUtil.pingable(""));
        Assertions.assertFalse(NetUtil.pingable(null));
    }
}
