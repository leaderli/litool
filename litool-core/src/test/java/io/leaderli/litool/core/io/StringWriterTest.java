package io.leaderli.litool.core.io;

import io.leaderli.litool.core.text.StringUtils;
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

        String repeat = StringUtils.repeat("1234567890", 10000);
        stringWriter = new StringWriter();

        stringWriter.write(repeat.getBytes());

        Assertions.assertEquals(repeat.length(), stringWriter.get().length());

    }
}
