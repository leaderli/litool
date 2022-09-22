package io.leaderli.litool.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author leaderli
 * @since 2022/9/22 7:38 PM
 */
public class MockInputStream extends InputStream {
    @Override
    public int read() throws IOException {
        return 0;
    }
}
