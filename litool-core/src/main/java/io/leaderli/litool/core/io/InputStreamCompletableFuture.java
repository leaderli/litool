package io.leaderli.litool.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.concurrent.*;

import static io.leaderli.litool.core.util.ConsoleUtil.print;

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
        CompletableFuture<Void> task = CompletableFuture.runAsync(this::read);
        try {
            print("task", task.get(3, TimeUnit.SECONDS));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            print(sb.toString());
        }
    }

    public void run(Executor executor) {
        CompletableFuture<Void> task = CompletableFuture.runAsync(this::read, executor);
        try {
            print("task", task.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
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
