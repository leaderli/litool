package io.leaderli.litool.core.io;

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

        InputStream stream = new ByteArrayInputStream("hello".getBytes());
        StringReader stringReader = new StringReader(stream);
        Assertions.assertEquals("hello", stringReader.get());
        Assertions.assertEquals("hello", stringReader.get());

        Assertions.assertThrows(IllegalStateException.class, () -> new StringReader(new InputStream() {
            @Override
            public int read() {
                return 0;
            }
        }));


    }

}
