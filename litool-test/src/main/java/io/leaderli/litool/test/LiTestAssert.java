package io.leaderli.litool.test;

import io.leaderli.litool.core.meta.Lira;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leaderli
 * @since 2022/9/30 5:16 PM
 */
public class LiTestAssert<T> {

    private final Lira<TestMatch<T>> testMatches;

    public LiTestAssert(List<TestMatch<T>> testMatches) {
        if (testMatches == null) {
            testMatches = new ArrayList<>();
        }
        this.testMatches = Lira.of(testMatches);
    }

    @SafeVarargs
    public LiTestAssert(TestMatch<T>... testMatches) {
        this.testMatches = Lira.of(testMatches);
    }

    public void test(T t) {

        boolean absent = Lira.of(testMatches)
                .filter(test -> test.match(t))
                .assertTrue(test -> test.test(t))
                .assertNoError().absent();

        if (absent) {
            throw new RuntimeException("not test assert found for " + t);
        }
    }
}
