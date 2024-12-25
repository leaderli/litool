package io.leaderli.litool.test;

public class MockException extends RuntimeException {
    public MockException(Throwable e) {
        super(e);
    }

    public MockException(String msg) {
        super(msg);
    }
}
