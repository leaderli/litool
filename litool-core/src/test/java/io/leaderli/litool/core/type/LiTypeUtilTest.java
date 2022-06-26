package io.leaderli.litool.core.type;

import io.leaderli.litool.core.util.LiPrintUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author leaderli
 * @since 2022/6/27
 */
class LiTypeUtilTest {

    @Test
    void isUnknown() {

        Assertions.assertTrue(LiTypeUtil.isUnknown(Consumer.class.getTypeParameters()[0]));
        LiPrintUtil.print((Object[]) Consumer.class.getTypeParameters());
        LiPrintUtil.print((Object[]) Function.class.getTypeParameters());
    }
}
