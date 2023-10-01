package io.leaderli.litool.core.io;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.function.Supplier;

/**
 * 从输出流中读取字符串的工具类
 *
 * @since 2022/9/22 3:52 PM
 */
public class StringWriter extends ByteArrayOutputStream implements Supplier<String> {


    private final Charset charset;

    public StringWriter() {
        this(Charset.defaultCharset());
    }

    public StringWriter(Charset charset) {
        this.charset = charset;
    }

    public String get() {
        return new String(buf, 0, size(), charset);
    }

    @Override
    public void flush() {
        super.reset();
    }
}
