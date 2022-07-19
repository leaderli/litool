package io.leaderli.litool.core.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

/**
 * @author leaderli
 * @since 2022/6/27
 */
class TypeUtilTest {

    @Test
    void isUnknown() {

        Assertions.assertTrue(TypeUtil.isUnknown(Consumer.class.getTypeParameters()[0]));
//        LiPrintUtil.print((Object[]) Consumer.class.getTypeParameters());
//        LiPrintUtil.print((Object[]) Function.class.getTypeParameters());
    }
}
