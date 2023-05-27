package io.leaderli.litool.core.io;

import io.leaderli.litool.core.concurrent.ErrorHandledFuture;

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
 * 提供获取脚本标准输出和错误输出的 {@link ErrorHandledFuture} 实现。对于一些像 {@code 'tail -f xxx'} 这样的脚本，
 * 它们不会停止输出，因此应使用 {@link ErrorHandledFuture#get(long, TimeUnit)} 来获取读取到的输入流，当继续
 * 使用此方法时，将获取到下一个到达的输入流。{@link ErrorHandledFuture#get()} 将始终获取完整的输入流内容。
 *
 * @see ErrorHandledFuture
 */
public class InputStreamCompletableFuture implements ErrorHandledFuture<String> {


    @SuppressWarnings("java:S1149")
    private final StringBuffer buffer = new StringBuffer();
    private final CompletableFuture<Void> task;
    private int index;

    public InputStreamCompletableFuture(InputStream inputStream) {
        this(inputStream, Charset.defaultCharset());
    }


    public InputStreamCompletableFuture(InputStream inputStream, Charset charset) {
        this.task = CompletableFuture.runAsync(() -> read(inputStream, charset));
    }

    /**
     * 读取给定的输入流，将其内容存储在 StringBuffer 中并返回其字符串表示形式。
     *
     * @param inputStream 待读取的输入流
     * @param charset     输入流的字符集编码
     * @return 输入流的字符串表示形式
     */
    private String read(InputStream inputStream, Charset charset) {
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

    /**
     * @return 获取输入流的结果
     * @see #task#get()
     */
    @SuppressWarnings("java:S2142")
    @Override
    public String get() {

        try {
            task.get();
        } catch (InterruptedException | ExecutionException ignore) {
        }
        return buffer.toString().trim();
    }

    /**
     * @return 获取输入流的结果, 如果超时了，则会将已经读取到的输入流返回，下一次再使用该方法，则从上一次读取的位置后继续读取。
     * @see #task#get(long, TimeUnit)
     */
    @SuppressWarnings("java:S2142")
    @Override
    public String get(long timeout, TimeUnit unit) {
        try {

            task.get(timeout, unit);

        } catch (ExecutionException | TimeoutException | InterruptedException ignore) {


        }
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
