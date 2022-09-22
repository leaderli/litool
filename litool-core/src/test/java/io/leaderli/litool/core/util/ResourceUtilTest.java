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
    void getResourceURLs() {


        Lira<URL> resourcesLira = ResourceUtil.getResourceURLs("io/leaderli");

        Assertions.assertEquals(2, resourcesLira.size());
        Assertions.assertTrue(ResourceUtil.getResourceURLs(null).absent());


//        Assertions.assertEquals(ResourceUtil.getResourceURLs(""), ResourceUtil.getResourceURLs("./"));
        Assertions.assertTrue(ResourceUtil.getResourceURLs(null).absent());

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

        resourceFile = ResourceUtil.getResourceFiles("io/leaderli/litool/core/bit", new WalkFileFilter() {
            @Override
            public boolean dir(File file) {
                return !file.getAbsolutePath().contains("test-classes");
            }

            @Override
            public boolean file(File file) {
                return !file.getName().contains("$");
            }
        });
        Assertions.assertEquals(5, resourceFile.size());
    }

    @Test
    void getResourceAsStream() {

        AutoCloseableUtil.closeableConsumer(() -> ResourceUtil.getResourceAsStream("io/leaderli/litool/core/bit" +
                "/BitStr" +
                ".class"), ins -> {


            StringBuilder sb = new StringBuilder();
            while (true) {
                int read = ins.read();
                if (read < 0) {
                    break;
                }
                sb.append(Integer.toHexString(read));
            }
            Assertions.assertTrue(sb.toString().startsWith("cafebabe"));
        });
//        System.out.println(StringUtils.read(resourceAsStream));
    }
}

