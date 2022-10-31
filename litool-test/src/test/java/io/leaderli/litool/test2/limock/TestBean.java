package io.leaderli.litool.test2.limock;

import java.util.List;

/**
 * @author leaderli
 * @since 2022/10/17 11:36 AM
 */
public class TestBean {
    public int m2(int length) {
        return 2;
    }

    public void m1() {
        System.out.println("m1");
    }

    public Foo m3() {

        return null;
    }

    public List<Foo> m4() {

        return null;
    }

    public Foo m5() {

        return new Foo();
    }

    public int m6() {
        return 100;
    }
}
