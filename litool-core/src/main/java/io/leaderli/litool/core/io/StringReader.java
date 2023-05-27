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
    private final StringBuilder sb = new StringBuilder();

    public StringReader(InputStream inputStream) {
        this(inputStream, Charset.defaultCharset());
    }

    public StringReader(InputStream inputStream, Charset charset) {
        this.inputStream = inputStream;
        this.charset = charset;
    }

    public String get() {
        read();
        return sb.toString();
    }

    private void read() {
        try (InputStreamReader reader = new InputStreamReader(this.inputStream, charset)) {
            int read;
            while ((read = inputStream.read()) != -1) {
                sb.append((char) read);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
