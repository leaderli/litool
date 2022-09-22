package io.leaderli.litool.core.io;

import io.leaderli.litool.core.util.RandomUtil;
import io.leaderli.litool.core.util.ThreadUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author leaderli
 * @since 2022/9/22 7:38 PM
 */
public class MockInfiniteInputStream extends InputStream {
    private static final String SEED = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ\n";

    private final long delay;
    private final Supplier<Integer> supplier;

    public MockInfiniteInputStream() {
        this.delay = 10;
        this.supplier = () -> (int) SEED.charAt(RandomUtil.nextInt(SEED.length()));
    }

    public MockInfiniteInputStream(long delay, Supplier<Integer> supplier) {
        this.delay = delay;
        this.supplier = supplier;
    }

    @Override
    public int read() throws IOException {
        Integer delay1 = ThreadUtil.delay(TimeUnit.MILLISECONDS, delay, supplier);
        if (delay1 == 10) {
            System.out.println(delay1);
            return '\r';
        }
        return delay1;
    }
}
