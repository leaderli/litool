package io.leaderli.litool.core.lang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author leaderli
 * @since 2022/9/22 9:24 AM
 */
class ShellTest {


    @Test
    void bash() throws ExecutionException, InterruptedException, TimeoutException {

        Future<String> command = new Shell().bash("123");
        Assertions.assertEquals("sh: 123: command not found", command.get());
        Assertions.assertEquals("sh: 123: command not found", command.get());


        command = new Shell().bash("echo 123 && sleep 1 && echo 456 && sleep 1");

        Assertions.assertEquals("123", command.get(50, TimeUnit.MILLISECONDS));
        Assertions.assertEquals("", command.get(100, TimeUnit.MILLISECONDS));
        Assertions.assertEquals("456", command.get(1, TimeUnit.SECONDS));
        command.cancel(true);

    }


}
