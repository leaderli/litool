package io.leaderli.litool.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

/**
 * @author leaderli
 * @since 2022/7/22
 */
class RegexUtilTest {

@Test
void delAll() {
    Assertions.assertEquals("", RegexUtil.delAll(Pattern.compile("\\d"), "123"));
    Assertions.assertEquals("123", RegexUtil.delAll(Pattern.compile("\\D"), "123"));
    Assertions.assertNull(RegexUtil.delAll(Pattern.compile("\\d"), null));
    Assertions.assertEquals("123", RegexUtil.delAll(null, "123"));
}

@Test
void contains() {

    Assertions.assertTrue(RegexUtil.contains(Pattern.compile("\\d"), "123"));
    Assertions.assertFalse(RegexUtil.contains(Pattern.compile("\\D"), "123"));
    Assertions.assertFalse(RegexUtil.contains(null, "123"));
    Assertions.assertFalse(RegexUtil.contains(Pattern.compile("\\D"), null));
}
}
