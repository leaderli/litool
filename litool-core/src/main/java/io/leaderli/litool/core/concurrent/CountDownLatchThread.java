package io.leaderli.litool.core.concurrent;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class CountDownLatchThread extends Thread {

    private final CountDownLatch countDownLatch;
    private final Runnable runnable;

    public CountDownLatchThread(CountDownLatch countDownLatch, Runnable runnable) {
        this.countDownLatch = countDownLatch;
        this.runnable = runnable;
    }

    @Override
    public void run() {
        try {
            this.runnable.run();
        } finally {
            this.countDownLatch.countDown();
        }
    }


    public static void execute(List<Runnable> runnables) throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(runnables.size());
        for (Runnable runnable : runnables) {
            new CountDownLatchThread(countDownLatch, runnable).start();
        }
        countDownLatch.await();
    }
}
