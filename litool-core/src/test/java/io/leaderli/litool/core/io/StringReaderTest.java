package io.leaderli.litool.core.io;

import io.leaderli.litool.core.text.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author leaderli
 * @since 2022/9/22 4:18 PM
 */
class StringReaderTest {

    @Test
    void get() {

        InputStream input = new ByteArrayInputStream("hello".getBytes());
        StringReader stringReader = new StringReader(input);
        Assertions.assertEquals("hello", stringReader.get());
        Assertions.assertEquals("hello", stringReader.get());

        String h1030 = StringUtils.repeat("h", 1030);
        input = new ByteArrayInputStream(h1030.getBytes());
        stringReader = new StringReader(input, Charset.defaultCharset(), 500);
        Assertions.assertEquals(h1030, stringReader.get());
        Assertions.assertEquals(h1030, stringReader.get());

        String z1000 = StringUtils.repeat("z1000", 1000);
        input = new ByteArrayInputStream(z1000.getBytes());
        stringReader = new StringReader(input, Charset.defaultCharset(), 500);
        Assertions.assertEquals(z1000, stringReader.get());
        Assertions.assertEquals(z1000, stringReader.get());

        input = new ByteArrayInputStream(h1030.getBytes()) {
            @Override
            public synchronized int read(byte[] b, int off, int len) {
                int read = super.read(b, off, len);
                if (read < 500) {
                    throw new IllegalStateException();
                }
                return read;
            }
        };
        StringReader errorReader = new StringReader(input, Charset.defaultCharset(), 500);
        Assertions.assertThrows(IllegalStateException.class, errorReader::get);
        Assertions.assertThrows(IllegalStateException.class, errorReader::get);

    }

}
