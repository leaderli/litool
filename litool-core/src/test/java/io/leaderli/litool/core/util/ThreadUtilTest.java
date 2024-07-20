package io.leaderli.litool.core.util;

import io.leaderli.litool.core.io.StringWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;

/**
 * @author leaderli
 * @since 2022/9/15 10:52 AM
 */
class ThreadUtilTest {

    @Test
    void printStack() {
        Assertions.assertEquals(1, ThreadUtil.delay(10, () -> 1));
        Assertions.assertDoesNotThrow(() -> ThreadUtil.printStack(0));
        Assertions.assertDoesNotThrow(() -> ThreadUtil.printStack(-100));
        Assertions.assertTrue(ThreadUtil.stackTrace(1).startsWith("io.leaderli.litool.core.util.ThreadUtilTest.printStack"));
        Assertions.assertDoesNotThrow(() -> ThreadUtil.stackTrace(-100));
        StringWriter out = new StringWriter();
        System.setOut(new PrintStream(out));
        ThreadUtil.println("test");
        ThreadUtil.printStack(1);
        Thread thread = Thread.currentThread();
        Assertions.assertTrue(out.get().startsWith(thread.getName()));
        Assertions.assertThrows(RuntimeException.class, () -> ThreadUtil.start(() -> {
            ThreadUtil.sleep(100);
            thread.interrupt();
        }, false));
        Thread thread1 = new Thread(ThreadUtil::join);
        thread1.start();

        Thread thread2 = new Thread(() -> {
            ThreadUtil.sleep(1000, () -> {
            });
        });
        thread2.start();
        ThreadUtil.sleep(20);
        thread2.interrupt();
        Thread thread3 = new Thread(() -> {
            ThreadUtil.sleep(1000);
        });
        thread3.start();
        ThreadUtil.sleep(20);
        thread3.interrupt();
    }
}
