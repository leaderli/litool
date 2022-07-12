package io.leaderli.litool.core.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author leaderli
 * @since 2022/7/12
 */
class LiIoUtilTest {

    @Test
    void getResourcesFile() throws IOException {


        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        System.out.println(loader.getResource("/"));

        System.out.println(LiIoUtil.class.getResource("/"));

        String relative = LiIoUtilTest.class.getName().replace(".", "/") + ".class";
        System.out.println(LiIoUtil.class.getClassLoader().getResource(relative));
        System.out.println(relative);
        System.out.println(LiIoUtil.class.getClassLoader().getResource(relative).getFile().replace(relative, ""));
        System.out.println(LiIoUtil.class.getResource("/").getFile());
    }
}
