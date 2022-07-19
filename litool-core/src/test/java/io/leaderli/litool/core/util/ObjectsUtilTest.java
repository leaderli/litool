package io.leaderli.litool.core.util;

import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/6/16 1:22 PM
 */
class ObjectsUtilTest {

    @Test
    void sameAny() {

        assert ObjectsUtil.sameAny(1, 1);
        assert ObjectsUtil.sameAny(1, 1, 2);
        assert ObjectsUtil.sameAny(1, 1, null);
        assert ObjectsUtil.sameAny(null, 1, null);
        assert !ObjectsUtil.sameAny(null, 1, 2);
        assert !ObjectsUtil.sameAny(3, 1, 2);
    }

}
