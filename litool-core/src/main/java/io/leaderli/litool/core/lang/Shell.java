package io.leaderli.litool.core.lang;

import io.leaderli.litool.core.concurrent.CompletedErrorHandledFuture;
import io.leaderli.litool.core.concurrent.ErrorHandledFuture;
import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.io.InputStreamCompletableFuture;
import io.leaderli.litool.core.meta.LiConstant;

import java.io.File;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Shell类用于执行shell命令。
 * 可以设置工作目录和字符集，提供了两个方法，分别用于执行bash脚本和命令。
 * bash方法用于执行bash脚本，命令内容通过参数传入，底层调用command方法执行。
 * command方法用于执行命令，参数为命令和命令参数。
 * 返回值为ErrorHandledFuture类对象，可以处理异步执行的结果和异常。
 */
public class Shell {

    private final File workDir;
    private final Charset charset;


    public Shell(File workDir) {
        this(workDir, Charset.defaultCharset());
    }

    public Shell(File workDir, Charset charset) {
        LiAssertUtil.assertTrue(workDir == null || workDir.exists() && workDir.isDirectory());
        this.workDir = workDir;
        this.charset = charset;
    }

    public Shell() {
        this(null, Charset.defaultCharset());
    }

    /**
     * @param command the bash script content
     * @return call {@link #command(String...)}  as {@code  command("sh", "-c", command)}
     */
    public ErrorHandledFuture<String> bash(String command) {
        return command(LiConstant.BASH, "-c", command);
    }

    /**
     * 执行脚本,第一个参数应该是程序,如{@code  "/bin/bash"},{@code  "/usr/local/python3"}
     *
     * @param commands 命令行参数
     * @return {@link  InputStreamCompletableFuture}。 如果{@link Process}构建错误，{@link  CompletedErrorHandledFuture}
     */
    public ErrorHandledFuture<String> command(String... commands) {

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(this.workDir);
        processBuilder.redirectErrorStream(true);
        processBuilder.command(commands);


        try {
            Process process = processBuilder.start();

            try (OutputStream outputStream = process.getOutputStream()) {
                outputStream.flush();
            }

            return new InputStreamCompletableFuture(process.getInputStream(), charset);

        } catch (Throwable e) {
            return new CompletedErrorHandledFuture<>("", e);
        }

    }

}
