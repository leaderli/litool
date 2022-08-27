package io.leaderli.litool.core.util;

import io.leaderli.litool.core.resource.PathUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leaderli
 * @since 2022/6/26
 */
class PathUtilTest {

@Test
void path_and_filename() {

    Assertions.assertEquals("1", PathUtil.get_file_name("/abc/1").get());
    Assertions.assertNull(PathUtil.get_file_name("/abc/").get());
    Assertions.assertEquals("1.log", PathUtil.get_file_name("/1.log").get());
    Assertions.assertEquals("1", PathUtil.get_file_name("1").get());
    Assertions.assertEquals("1.log", PathUtil.get_file_name("1.log").get());
}


}
