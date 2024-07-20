package io.leaderli.litool.core.io;

import io.leaderli.litool.core.text.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

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

        Assertions.assertThrows(IllegalStateException.class, () -> new StringReader(new InputStream() {
            @Override
            public int read() {
                return 0;
            }
        }));


        String h1030 = StringUtils.repeat("h", 1030);
        input = new ByteArrayInputStream(h1030.getBytes());
        stringReader = new StringReader(input);
        Assertions.assertEquals(h1030, stringReader.get());
        Assertions.assertEquals(h1030, stringReader.get());
    }

}
