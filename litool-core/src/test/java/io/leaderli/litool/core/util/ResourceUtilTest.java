package io.leaderli.litool.core.util;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.resource.ResourceUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URL;

/**
 * @author leaderli
 * @since 2022/7/12
 */
class ResourceUtilTest {


    @Test
    void getResource() {


        Assertions.assertEquals(ResourceUtil.getResource("/"), ResourceUtil.getResource(""));
        Assertions.assertEquals(ResourceUtil.getResource("/"), ResourceUtil.getResource(null));
        Assertions.assertEquals(ResourceUtil.getResource("/", ResourceUtil.class), ResourceUtil.getResource(""));
        Assertions.assertNotEquals(ResourceUtil.getResource("", ResourceUtil.class), ResourceUtil.getResource(""));
    }

    @Test
    void getResourceIter() {

        Lira<URL> resourcesLira = ResourceUtil.getResourcesLira("io/leaderli");

        Assertions.assertEquals(2, resourcesLira.size());
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
