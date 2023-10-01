package io.leaderli.litool.core.io;

import io.leaderli.litool.core.resource.ResourceUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

class IOUtilsTest {


    @Test
    void test() throws IOException {

        InputStream in = ResourceUtil.createContentStream("123");
        StringWriter out = new StringWriter();
        IOUtils.copy(in, out);
        Assertions.assertEquals("123", out.get());

        StringWriter out1 = new StringWriter();
        StringWriter out2 = new StringWriter();
        in = ResourceUtil.createContentStream("123");
        IOUtils.copy(in, out1, out2);
        Assertions.assertEquals("123", out1.get());
        Assertions.assertEquals("123", out2.get());
    }
}
