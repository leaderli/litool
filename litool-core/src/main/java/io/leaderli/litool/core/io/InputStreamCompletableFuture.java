package io.leaderli.litool.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.concurrent.*;

/**
 * @author leaderli
 * @since 2022/9/22 3:19 PM
 */
public class InputStreamCompletableFuture implements Future<String> {

    private final StringBuffer sb = new StringBuffer();
    final Object lock = new Object();

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
                synchronized (lock) {
                    if ((char) read == ',') {
                        lock.notify();
                    }
                    sb.append((char) read);
                }
            }
//            print("notify all");
            lock.notify();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return sb.toString();
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
    public String get() throws ExecutionException, InterruptedException {

        return task.get();
    }

    @Override
    public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        try {
            return task.get(timeout, unit);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            String result;
            synchronized (lock) {
                if (!task.isDone() && !sb.toString().endsWith(",")) {
                    lock.wait();
                }
            }
            synchronized (sb) {

                result = sb.toString();
                sb.setLength(0);
            }
            return "timeout:" + result;

        }
    }


}
