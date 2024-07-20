package io.leaderli.litool.core.io;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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
    private final byte[] buffer;
    private final byte[] sb = new byte[0];
    private final ByteArrayOutputStream byteArrayInputStream;
    private RuntimeException readException;


    public StringReader(InputStream inputStream) {
        this(inputStream, Charset.defaultCharset());
    }

    public StringReader(InputStream inputStream, Charset charset) {
        this(inputStream, charset, 1024);
    }

    public StringReader(InputStream inputStream, Charset charset, int bufSize) {
        this.inputStream = inputStream;
        this.charset = charset;
        buffer = new byte[bufSize];
        byteArrayInputStream = new ByteArrayOutputStream(bufSize);
    }

    public String get() {
        if (readException != null) {
            throw readException;
        }
        read();
        return new String(byteArrayInputStream.toByteArray(), charset);
    }

    private void read() {
        int count;
        try {
            while (inputStream.available() > 0 && (count = inputStream.read(buffer)) != -1) {
                byteArrayInputStream.write(buffer, 0, count);
            }

        } catch (RuntimeException e) {
            readException = e;
            throw readException;
        } catch (Exception e) {
            readException = new RuntimeException(e);
            throw readException;
        }
    }

}
