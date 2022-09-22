package io.leaderli.litool.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author leaderli
 * @since 2022/9/22 3:19 PM
 */
public class InputStreamCompletableFuture {

    private final InputStream inputStream;
    private final Charset charset;
    private final StringBuffer sb = new StringBuffer();

    public InputStreamCompletableFuture(InputStream inputStream) {
        this(inputStream, Charset.defaultCharset());
    }

    public InputStreamCompletableFuture(InputStream inputStream, Charset charset) {
        this.inputStream = inputStream;
        this.charset = charset;

    }

    public void run() {
        run(null);
    }

    public void run(Executor executor) {
        CompletableFuture<Void> task = CompletableFuture.runAsync(this::read, executor);
//        task.get()
        System.out.println(sb);
    }

    public void read() {
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
