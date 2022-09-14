package io.leaderli.litool.core.meta.logic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

class LiLogicPipeLineTest {

    @Test
    void test() {


        assert !LiLogicPipeLine.begin().test(str -> true).and().test(str -> false).apply("");


        assert !LiLogicPipeLine.begin().test(str -> false).and().test(str -> false).apply("hello");


        assert !LiLogicPipeLine.begin().test(str -> false).and().test(str -> true).apply("hello");


        assert LiLogicPipeLine.begin().test(str -> true).and().test(str -> true).apply("hello");


        assert LiLogicPipeLine.begin().test(str -> true).or().test(str -> false).apply("hello");


        assert !LiLogicPipeLine.begin().test(str -> false).or().test(str -> false).apply("hello");


        assert LiLogicPipeLine.begin().test(str -> false).or().test(str -> true).apply("hello");


        assert LiLogicPipeLine.begin().test(str -> true).or().test(str -> true).apply("hello");


        assert LiLogicPipeLine.begin().test(str -> true).apply("hello");


        assert !LiLogicPipeLine.begin().test(str -> false).apply("hello");


        assert !LiLogicPipeLine.begin().not().test(str -> true).apply("hello");


        assert LiLogicPipeLine.begin().not().test(str -> false).apply("hello");


        LiLogicPipeLine.begin().test(str -> false);
        assert LiLogicPipeLine.<String>begin().not().test(str -> false).and().not().test(str -> false).apply("hello");


        LiLogicPipeLine<Object> or = (LiLogicPipeLine<Object>) LiLogicPipeLine.begin().test(str -> false).or();
        Assertions.assertFalse(or.apply("123"));

    }

    @Test
    void test2() {
        assert !MyLiLogicPipeLine.instance().test(str -> true).and().test(str -> false).apply("1");
        assert !MyLiLogicPipeLine.instance().len(1).and().len(2).apply("1");
        assert MyLiLogicPipeLine.instance().len(1).or().len(2).apply("1");
    }

    private interface MyInterPredicateSink extends UnionOperation<String> {
        @Override
        MyTestCombineOperation and();

        @Override
        MyTestCombineOperation or();

    }

    private interface MyTestOperation extends TestOperation<String> {
        MyInterPredicateSink len(int size);

        @Override
        MyInterPredicateSink test(Function<String, ?> predicate);
    }

    @FunctionalInterface
    private interface MyInterNotOperationSink extends NotOperation<String> {
        @Override
        MyTestOperation not();
    }


    private interface MyTestCombineOperation extends MyTestOperation, MyInterNotOperationSink,
            CombineOperation<String> {
    }

    private interface MyTestLogicPipeLine extends MyTestCombineOperation, MyInterPredicateSink {

    }

    private static class MyLiLogicPipeLine implements MyTestLogicPipeLine {

        private final LiLogicPipeLine<String> proxy = (LiLogicPipeLine<String>) LiLogicPipeLine.<String>begin();

        private MyLiLogicPipeLine() {

        }

        public static MyLiLogicPipeLine instance() {

            return new MyLiLogicPipeLine();
        }

        @Override
        public MyTestCombineOperation and() {
            proxy.and();
            return this;
        }

        @Override
        public MyTestCombineOperation or() {
            proxy.or();
            return this;
        }

        @Override
        public MyTestOperation not() {
            proxy.not();
            return this;
        }

        @Override
        public Boolean apply(String s) {
            return proxy.apply(s);
        }

        @Override
        public MyInterPredicateSink len(int size) {
            test(str -> size == str.length());
            return this;
        }

        @Override
        public MyInterPredicateSink test(Function<String, ?> predicate) {
            proxy.test(predicate);
            return this;
        }


    }
}
