package io.leaderli.litool.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leaderli
 * @since 2022/9/15 8:45 AM
 */
class HumanSizeUtilTest {

    @Test
    void test() {
        Map<Long, String> test = new HashMap<Long, String>() {{
            put(0L, "0 Bytes");
            put(1023L, "1023 Bytes");
            put(1024L, "1 KB");
            put(12_345L, "12.06 KB");
            put(10_123_456L, "9.65 MB");
            put(10_123_456_798L, "9.43 GB");
            put(1_777_777_777_777_777_777L, "1.54 EB");
        }};

        test.forEach((in, expected) -> Assertions.assertEquals(expected, HumanSizeUtil.convertToHumanReadableSize(in)));

        Assertions.assertThrows(IllegalArgumentException.class, () -> HumanSizeUtil.convertToHumanReadableSize(-1));

    }


}
