package io.leaderli.litool.core.concurrent;

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
}
