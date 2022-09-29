package io.leaderli.litool.core.lang;

import io.leaderli.litool.core.concurrent.CatchFuture;
import io.leaderli.litool.core.concurrent.CompletedCatchFuture;
import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.io.InputStreamCompletableFuture;
import io.leaderli.litool.core.meta.LiConstant;

import java.io.File;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * @author leaderli
 * @since 2022/9/22 8:46 AM
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
    public CatchFuture<String> bash(String command) {
        return command(LiConstant.BASH, "-c", command);
    }

    /**
     * @param commands the script, the first parameter should be the program,
     *                 such as {@code  "/bin/bash"}, {@code  "/usr/local/python3"}
     * @return a {@link  InputStreamCompletableFuture} , or {@link  CompletedCatchFuture} if the {@link  Process}
     * build error
     */
    public CatchFuture<String> command(String... commands) {

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
            return new CompletedCatchFuture<>("", e);
        }

    }

}
