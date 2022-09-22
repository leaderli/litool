package io.leaderli.litool.core.lang;

import io.leaderli.litool.core.bit.BitState;
import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.LiBox;
import io.leaderli.litool.core.util.ThreadUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static io.leaderli.litool.core.util.ConsoleUtil.line;

/**
 * @author leaderli
 * @since 2022/9/22 8:46 AM
 */
public class Shell extends BitState {

    public static final int INIT = 0;
    public static final int SUCCESS = 1;
    public static final int ERROR = 1 << 1;
    final StringBuffer sb = new StringBuffer();
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

    public void command(String... commands) {

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(this.workDir);
        processBuilder.command(commands);

        try {
            Process process = processBuilder.start();

            try (OutputStream outputStream = process.getOutputStream()) {
                outputStream.flush();
            }


            CompletableFuture<String> futureTask = CompletableFuture.supplyAsync(

                    () -> {

                        try {
                            InputStreamReader inputStream = new InputStreamReader(process.getInputStream(), Charset.defaultCharset());


                            while (true) {
                                int read = inputStream.read();
                                if (read == -1) break;
                                sb.append((char) read);
                            }
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }

                        return sb.toString();
                    });


            new Thread(() -> {

                LiBox<Integer> count = LiBox.of(0);
                for (; ; ) {

                    synchronized (sb) {

                        if (sb.length() > 0) {
                            System.out.print(sb);
                            sb.setLength(0);
                        }
                    }
                    ThreadUtil.sleep(TimeUnit.MILLISECONDS, 500, () -> System.out.println(count));
                }

            }).start();


            line();
            futureTask.get();

            ThreadUtil.join();

        } catch (IOException e) {
            enable(ERROR);
            e.printStackTrace();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
