package io.leaderli.litool.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/7/12
 */
class LiResourceUtilTest {

    @Test
    void getResourcesFile() {


        Assertions.assertNotNull(LiResourceUtil.getResourceAsStream("/"));


        Assertions.assertEquals(1, LiResourceUtil.getResourcesFile(null).size());


    }
}
