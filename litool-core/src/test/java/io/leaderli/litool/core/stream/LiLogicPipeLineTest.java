package io.leaderli.litool.core.stream;

import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

class LiLogicPipeLineTest {

    @SuppressWarnings("AssertWithSideEffects")
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

    }

    @Test
    void test2() {
        assert !MyLiLogicPipeLine.instance().test(str -> true).and().test(str -> false).apply("1");
        assert !MyLiLogicPipeLine.instance().len(1).and().len(2).apply("1");
        assert MyLiLogicPipeLine.instance().len(1).or().len(2).apply("1");
    }

    private interface MyInterPredicateSink extends InterPredicateSink<String> {
        @Override
        MyInterCombineOperationSink and();

        @Override
        MyInterCombineOperationSink or();

    }

    private interface MyInterOperationSink extends InterOperationSink<String> {
        MyInterPredicateSink len(int size);

        @Override
        MyInterPredicateSink test(Predicate<String> predicate);
    }

    @FunctionalInterface
    private interface MyInterNotOperationSink extends InterNotOperationSink<String> {
        @Override
        MyInterOperationSink not();
    }


    private interface MyInterCombineOperationSink extends MyInterOperationSink, MyInterNotOperationSink,
            InterCombineOperationSink<String> {
    }

    private interface MyInterLogicPipeLineSink extends MyInterCombineOperationSink, MyInterPredicateSink {

    }

    private static class MyLiLogicPipeLine implements MyInterLogicPipeLineSink {

        private final LiLogicPipeLine<String> proxy = (LiLogicPipeLine<String>) LiLogicPipeLine.<String>begin();

        private MyLiLogicPipeLine() {

        }

        public static MyLiLogicPipeLine instance() {

            return new MyLiLogicPipeLine();
        }

        @Override
        public MyInterCombineOperationSink and() {
            proxy.and();
            return this;
        }

        @Override
        public MyInterCombineOperationSink or() {
            proxy.or();
            return this;
        }

        @Override
        public MyInterOperationSink not() {
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
        public MyInterPredicateSink test(Predicate<String> predicate) {
            proxy.test(predicate);
            return this;
        }


    }
}
