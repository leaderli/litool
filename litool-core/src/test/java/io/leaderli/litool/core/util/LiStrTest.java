package io.leaderli.litool.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author leaderli
 * @since 2022/6/15
 */
class LiStrTest {

    @Test
    public void ljust() {

        Assertions.assertEquals("***1",LiStr.ljust("1",4,"*"));
        Assertions.assertEquals("   1",LiStr.ljust("1",4));
        Assertions.assertEquals("    ",LiStr.ljust(null,4));
        Assertions.assertEquals("12345",LiStr.ljust("12345",4));
    }
    @Test
    public void rjust() {

        Assertions.assertEquals("1***",LiStr.rjust("1",4,"*"));
        Assertions.assertEquals("1   ",LiStr.rjust("1",4));
        Assertions.assertEquals("    ",LiStr.rjust(null,4));
        Assertions.assertEquals("12345",LiStr.rjust("12345",4));
    }
}
