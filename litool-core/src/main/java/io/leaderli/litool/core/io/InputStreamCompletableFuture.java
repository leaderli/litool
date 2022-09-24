package io.leaderli.litool.core.io;

import io.leaderli.litool.core.concurrent.CatchFuture;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * {@link  CatchFuture} provide {@link  CatchFuture#get()}, {@link CatchFuture#get(long, TimeUnit)}
 * to get the script stdout, stderr.  some script suck as {@code  'tail -f xxx'} will never stop.
 * so it's should use {@link  CatchFuture#get(long, TimeUnit)} to get arrived inputStream. when continue
 * use this method, will get the next arrived inputStream. {@link  CatchFuture#get()} will always get
 * the full inputStream content
 *
 * @author leaderli
 * @since 2022/9/22 3:19 PM
 */
public class InputStreamCompletableFuture implements CatchFuture<String> {

    private final StringBuffer buffer = new StringBuffer();
    private int index;

    public InputStreamCompletableFuture(InputStream inputStream) {
        this(inputStream, Charset.defaultCharset());
    }

    private final CompletableFuture<String> task;


    public InputStreamCompletableFuture(InputStream inputStream, Charset charset) {
        this.task = CompletableFuture.supplyAsync(() -> read(inputStream, charset));
    }

    String read(InputStream inputStream, Charset charset) {
        try (InputStreamReader reader = new InputStreamReader(inputStream, charset)) {
            int read;
            while ((read = inputStream.read()) != -1) {
                buffer.append((char) read);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return buffer.toString().trim();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {

        return task.cancel(mayInterruptIfRunning);

    }

    @Override
    public boolean isCancelled() {
        return task.isCancelled();
    }

    @Override
    public boolean isDone() {
        return task.isDone();
    }

    @Override
    public String get() {

        try {
            return task.get();
        } catch (InterruptedException | ExecutionException e) {
            return buffer.toString().trim();
        }
    }

    @Override
    public String get(long timeout, TimeUnit unit) {
        try {
            return task.get(timeout, unit);
        } catch (ExecutionException | TimeoutException | InterruptedException e) {
            synchronized (buffer) {
                String result = buffer.substring(index);
                index = buffer.length();
                if (index < 0) {
                    index = 0;
                    buffer.setLength(0);
                }
                return result.trim();
            }

        }
    }


}
