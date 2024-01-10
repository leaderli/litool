package io.leaderli.litool.core.io;

import io.leaderli.litool.core.util.RandomUtil;

import java.io.InputStream;
import java.util.function.Supplier;

/**
 * @author leaderli
 * @since 2022/9/22 7:38 PM
 */
@SuppressWarnings("java:S4929")
public class MockInfiniteInputStream extends InputStream {
    private static final byte[] SEED = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ\n".getBytes();

    private final Supplier<Integer> supplier;

    public MockInfiniteInputStream() {
        this.supplier = () -> (int) SEED[(RandomUtil.nextInt(SEED.length))];
    }

    public MockInfiniteInputStream(Supplier<Integer> supplier) {
        this.supplier = supplier;
    }

    @Override
    public int read() {
        // don not use sleep
        return supplier.get();
    }


}
