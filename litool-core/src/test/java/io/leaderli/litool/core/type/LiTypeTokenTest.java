package io.leaderli.litool.core.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/9/24 3:23 PM
 */
class LiTypeTokenTest {

    @Test
    void test() {

        Assertions.assertNotEquals(LiTypeToken.get(int.class), LiTypeToken.get(Integer.class));

    }

}
