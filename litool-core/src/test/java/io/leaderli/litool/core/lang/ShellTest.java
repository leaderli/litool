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
        Assertions.assertTrue(command.get().contains("not found"));
        Assertions.assertTrue(command.get().contains("not found"));


        command = new Shell().bash("echo 123 && sleep 1 && echo 4 && sleep 1");


        Assertions.assertEquals("123", command.get(500, TimeUnit.MILLISECONDS));
        Assertions.assertEquals("", command.get(100, TimeUnit.MILLISECONDS));
        Assertions.assertEquals("4", command.get(1, TimeUnit.SECONDS));
        Assertions.assertEquals("", command.get(1, TimeUnit.SECONDS));
        command.cancel(true);

    }


}
