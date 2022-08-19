package io.leaderli.litool.core.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/7/22
 */
class LiPrimitiveTest {

    @Test
    void def() {

        Assertions.assertEquals(0, LiPrimitive.def(int.class));
        Assertions.assertEquals(0, LiPrimitive.def(Integer.class));
        Assertions.assertNull(LiPrimitive.def(String.class));
    }




}
