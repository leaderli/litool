package io.leaderli.litool.core.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FileUtilTest {


    @Test
    void test() {

        Assertions.assertNotNull(FileUtil.getHome());
        Assertions.assertTrue(FileUtil.getHomeFile().isDirectory());

    }



}
