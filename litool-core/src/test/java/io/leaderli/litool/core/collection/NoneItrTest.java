package io.leaderli.litool.core.collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/8/27
 */
class NoneItrTest {


@Test
void same() {
    Assertions.assertTrue(NoneItr.same(NoneItr.of()));

}


}
