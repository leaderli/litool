package io.leaderli.litool.core.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * @author leaderli
 * @since 2022/8/28
 */
class FileNameUtilTest {

@Test
void getName() {

    Assertions.assertNull(FileNameUtil.getName((File) null));
    Assertions.assertNull(FileNameUtil.getName((String) null));

    Assertions.assertEquals("test", FileNameUtil.getName(new File("test")));
    Assertions.assertEquals("test", FileNameUtil.getName("test"));
    Assertions.assertEquals("test", FileNameUtil.getName("test" + File.separator));
    Assertions.assertEquals("test.jpg", FileNameUtil.getName("test.jpg"));
    Assertions.assertEquals("test.jpg", FileNameUtil.getName("/test.jpg"));

}

@Test
void extName() {
    File file = new File("test.java");
    Assertions.assertEquals("java", FileNameUtil.extName(file));
    file = new File("");
    Assertions.assertEquals("", FileNameUtil.extName(file));
    file = new File(".");
    Assertions.assertNull(FileNameUtil.extName(file));
    file = new File("/");
    Assertions.assertNull(FileNameUtil.extName(file));

    Assertions.assertNull(FileNameUtil.extName((File) null));
    Assertions.assertNull(FileNameUtil.extName((String) null));
    Assertions.assertEquals("", FileNameUtil.extName(""));
    Assertions.assertEquals("", FileNameUtil.extName("."));
    Assertions.assertNull(FileNameUtil.extName(File.separator));
    Assertions.assertEquals("java", FileNameUtil.extName("test.java"));
    Assertions.assertEquals("", FileNameUtil.extName("java"));
}


@Test
void isType() {
    Assertions.assertTrue(FileNameUtil.isType(".java", "jar", "java"));
    Assertions.assertFalse(FileNameUtil.isType("", "jar", "java"));
    Assertions.assertFalse(FileNameUtil.isType(null, "jar", "java"));
    Assertions.assertFalse(FileNameUtil.isType("jar"));
}
}
