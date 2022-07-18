package io.leaderli.litool.core.util;

import io.leaderli.litool.core.meta.Lira;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URL;

/**
 * @author leaderli
 * @since 2022/7/12
 */
class LiResourceUtilTest {


    @Test
    void getResource() {


        Assertions.assertEquals(LiResourceUtil.getResource("/"), LiResourceUtil.getResource(""));
        Assertions.assertEquals(LiResourceUtil.getResource("/"), LiResourceUtil.getResource(null));
        Assertions.assertEquals(LiResourceUtil.getResource("/", LiResourceUtil.class), LiResourceUtil.getResource(""));
        Assertions.assertNotEquals(LiResourceUtil.getResource("", LiResourceUtil.class), LiResourceUtil.getResource(""));
    }

    @Test
    void getResourceIter() {

        Lira<URL> resourcesLira = LiResourceUtil.getResourcesLira("io/leaderli");
        System.out.println(resourcesLira.getRaw());
//        LiPrintUtil.println(LiResourceUtil.getResourceIter("").getRaw());
//
//        EnumerationIter.of(LiClassLoaderUtil.getClassLoader().getResources("")).forEachRemaining(
//                System.out::println
//        );
//        System.out.println("------------------");
//        Assertions.assertNotNull(LiResourceUtil.getResourceAsStream("/"));
//
//
////        System.out.println(LiResourceUtil.getResourcesFile(null).getRaw());
//        LiIterator.of(LiClassLoaderUtil.getClassLoader().getResources(""))
//                .forEachRemaining(System.out::println);
//        Assertions.assertEquals(1, LiResourceUtil.getResourceFile(null).size());


    }
}
