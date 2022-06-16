package io.leaderli.litool.core.util;

import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/6/16 1:22 PM
 */
class LiObjUtilTest {

    @Test
    void sameAny() {

        assert LiObjUtil.sameAny(1, 1);
        assert LiObjUtil.sameAny(1, 1, 2);
        assert LiObjUtil.sameAny(1, 1, null);
        assert LiObjUtil.sameAny(null, 1, null);
        assert !LiObjUtil.sameAny(null, 1, 2);
        assert !LiObjUtil.sameAny(3, 1, 2);
    }

}
