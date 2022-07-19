package io.leaderli.litool.core.util;

import io.leaderli.litool.core.meta.LiConstant;
import io.leaderli.litool.core.text.StringConvert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/7/9 2:12 PM
 */
class StringConvertTest {

    @Test
    void parserInt() {

        LiConstant.WHEN_THROW = null;
        Assertions.assertSame(0, StringConvert.parser(null, 0));
        Assertions.assertSame(1, StringConvert.parser("1", 0));
        Assertions.assertSame(true, StringConvert.parser("true", false));
        Assertions.assertSame((byte) 1, StringConvert.parser("1", (byte) 0));

        Assertions.assertEquals(1d, StringConvert.parser("1", 0d));
        Assertions.assertEquals(1f, StringConvert.parser("1", 0f));
        Assertions.assertSame(1, StringConvert.parser("1", 0));
        Assertions.assertSame(1L, StringConvert.parser("1", 0L));
        Assertions.assertSame((short) 1, StringConvert.parser("1", (short) 0));
    }


}
