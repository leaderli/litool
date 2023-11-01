package io.leaderli.litool.core.io;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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
        this.charset = Charset.defaultCharset();
    }

    public StringWriter(int size) {
        super(size);
        this.charset = Charset.defaultCharset();
    }

    public StringWriter(Charset charset) {
        this.charset = charset;
    }

    public StringWriter(Charset charset, int size) {
        super(size);
        this.charset = charset;
    }

    public String get() {
        return new String(buf, 0, size(), charset);
    }

    @Override
    public void flush() {
        super.reset();
    }

    /**
     * @return new PrintStream(this);
     */
    public PrintStream printStream() {
        return new PrintStream(this);
    }
}
