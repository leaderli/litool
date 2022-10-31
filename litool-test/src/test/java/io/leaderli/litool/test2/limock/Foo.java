package io.leaderli.litool.test2.limock;

/**
 * @author leaderli
 * @since 2022/10/17 11:36 AM
 */
public class Foo {
    public int init(int a, int b) {
        return a + b;
    }

    public int notCall(int a, int b) {
        return a + b;
    }

    public void run() {

    }

    public static Foo instance() {
        return new Foo();
    }
}
