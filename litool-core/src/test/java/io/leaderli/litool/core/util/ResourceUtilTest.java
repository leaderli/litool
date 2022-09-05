package io.leaderli.litool.core.util;

import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.resource.ResourceUtil;
import io.leaderli.litool.core.resource.WalkFileFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
    void getResourceItr() {


        Lira<URL> resourcesLira = ResourceUtil.getResourcesLira("io/leaderli");

        Assertions.assertEquals(2, resourcesLira.size());
//        LiPrintUtil.println(LiResourceUtil.getResourceIter("").get());
//
//        EnumerationIter.of(LiClassLoaderUtil.getClassLoader().getResources("")).forEachRemaining(
//                System.out::println
//        );
//        System.out.println("------------------");
//        Assertions.assertNotNull(LiResourceUtil.getResourceAsStream("/"));
//
//
////        System.out.println(LiResourceUtil.getResourcesFile(null).get());
//        LiIterator.of(LiClassLoaderUtil.getClassLoader().getResources(""))
//                .forEachRemaining(System.out::println);
//        Assertions.assertEquals(1, LiResourceUtil.getResourceFile(null).size());


    }

    @Test
    void createContentStream() throws IOException {

        InputStream contentStream = ResourceUtil.createContentStream(null);
        Assertions.assertEquals(0, contentStream.available());
        contentStream = ResourceUtil.createContentStream("hello");
        Assertions.assertEquals(5, contentStream.available());
    }

    @Test
    void getResourceFile() {


        Lira<File> resourceFile = ResourceUtil.getResourceFiles(pathname -> !pathname.getName().endsWith(".class"));

        Assertions.assertTrue(resourceFile.absent());
        resourceFile = ResourceUtil.getResourceFiles(
                new WalkFileFilter() {
                    @Override
                    public boolean dir(File dir) {
                        return false;
                    }

                    @Override
                    public boolean file(File file) {
                        return true;
                    }
                }


        );

        Assertions.assertTrue(resourceFile.absent());
        ConsoleUtil.println(resourceFile);
        System.out.println("---");

        resourceFile = ResourceUtil.getResourceFiles("io/leaderli/litool/core/bit", new WalkFileFilter() {
            @Override
            public boolean dir(File file) {

                System.out.println(file);
                return false;
            }

            @Override
            public boolean file(File file) {
                return !file.getName().contains("$") && !file.getPath().contains("target/test-classes");
            }
        });
        Assertions.assertEquals(4, resourceFile.size());
    }

    @Test
    void getResourcesLira() {


        Assertions.assertTrue(ResourceUtil.getResourcesLira(null).absent());
        Assertions.assertEquals(2, ResourceUtil.getResourcesLira("").size());
    }

}
