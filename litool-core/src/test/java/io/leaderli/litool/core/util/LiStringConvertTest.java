package io.leaderli.litool.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/7/9 2:12 PM
 */
class LiStringConvertTest {

    @Test
    void parserInt() {

        Assertions.assertSame(0, LiStringConvert.parser(null, 0));
        Assertions.assertSame(1, LiStringConvert.parser("1", 0));
        Assertions.assertSame(true, LiStringConvert.parser("true", false));
        Assertions.assertSame((byte) 1, LiStringConvert.parser("1", (byte) 0));

        Assertions.assertEquals(1d, LiStringConvert.parser("1", 0d));
        Assertions.assertEquals(1f, LiStringConvert.parser("1", 0f));
        Assertions.assertSame(1, LiStringConvert.parser("1", 0));
        Assertions.assertSame(1L, LiStringConvert.parser("1", 0L));
        Assertions.assertSame((short) 1, LiStringConvert.parser("1", (short) 0));
    }


}
