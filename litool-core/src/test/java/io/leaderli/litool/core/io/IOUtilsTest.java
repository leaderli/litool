package io.leaderli.litool.core.io;

import io.leaderli.litool.core.meta.LiBox;
import io.leaderli.litool.core.resource.ResourceUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

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

    @Test
    void testEmptyPrintStream() {
        LiBox<Integer> box = LiBox.none();
        System.setErr(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                box.value(b);
            }
        }));
        System.err.print(1);
        Assertions.assertEquals(49, box.value());
        box.reset();

        Assertions.assertNull(box.value());
        System.setErr(IOUtils.emptyPrintStream());
        System.err.print(1);
        Assertions.assertNull(box.value());
    }
}
