package io.leaderli.litool.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.function.Supplier;

/**
 * 从输入流中读取字符串的工具类
 *
 * @since 2022/9/22 3:52 PM
 */
public class StringReader implements Supplier<String> {


    private final InputStream inputStream;
    private final Charset charset;
    private final char[] buffer;

    private final StringBuilder sb = new StringBuilder();

    public StringReader(InputStream inputStream) {
        this(inputStream, Charset.defaultCharset());
    }

    public StringReader(InputStream inputStream, Charset charset) {
        this(inputStream, charset, 1024);
    }

    public StringReader(InputStream inputStream, Charset charset, int bufSize) {
        this.inputStream = inputStream;
        try {
            if (this.inputStream.available() <= 0) {
                throw new IllegalStateException("inputstream is unavaliable");
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        this.charset = charset;
        buffer = new char[bufSize];
    }

    public String get() {
        read();
        return sb.toString();
    }

    private void read() {

        try (InputStreamReader reader = new InputStreamReader(this.inputStream, charset)) {

            int count;
            while ((count = reader.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, count));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
