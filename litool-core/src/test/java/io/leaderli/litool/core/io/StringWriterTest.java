package io.leaderli.litool.core.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class StringWriterTest {


    @Test
    void test() throws IOException {

        String text = "中文";
        StringWriter stringWriter = new StringWriter();
        stringWriter.write(text.getBytes());
        Assertions.assertEquals(text, stringWriter.get());
        stringWriter.write(text.getBytes());
        Assertions.assertEquals(text + text, stringWriter.get());
        stringWriter.flush();
        Assertions.assertEquals("", stringWriter.get());

    }
}
