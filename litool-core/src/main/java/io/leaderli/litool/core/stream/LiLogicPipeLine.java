package io.leaderli.litool.core.stream;

import java.util.function.Predicate;

public class LiLogicPipeLine<T> implements LinterLogicPipeLineSink<T> {


    private static class Head<T> extends LiSink<T, Boolean> {

        public Head() {
            super(null);
        }

        @Override
        public Boolean apply(T request, Boolean last) {
            return next(request, LiPredicateSink.NO_NOT_OPERATION);
        }
    }

    private LiSink<T, Boolean> liSink;

    private LiLogicPipeLine() {

    }


    public static <T> LinterCombineOperationSink<T> begin() {

        LiLogicPipeLine<T> logic = new LiLogicPipeLine<>();
        logic.liSink = new Head<>();
        return logic;
    }

    @Override
    public Boolean apply(T t) {
        return liSink.request(t);
    }

    @Override
    public LinterOperationSink<T> not() {
        this.liSink = new LiSink<T, Boolean>(this.liSink) {
            @Override
            public Boolean apply(T request, Boolean last) {
                return next(request, LiPredicateSink.IS_NOT_OPERATION);
            }
        };
        return this;
    }

    @Override
    public LinterPredicateSink<T> test(Predicate<T> predicate) {

        this.liSink = new LiPredicateSink<>(this.liSink, predicate);

        return this;
    }


    @Override
    public LinterCombineOperationSink<T> and() {

        this.liSink = new LiSink<T, Boolean>(this.liSink) {
            @Override
            public Boolean apply(T request, Boolean lastPredicateResult) {
                //短路
                if (Boolean.TRUE.equals(lastPredicateResult)) {
                    return next(request, LiPredicateSink.NO_NOT_OPERATION);
                }
                return false;
            }
        };
        return this;
    }

    @Override
    public LinterCombineOperationSink<T> or() {
        this.liSink = new LiSink<T, Boolean>(this.liSink) {
            @Override
            public Boolean apply(T request, Boolean lastPredicateResult) {
                //短路
                if (Boolean.TRUE.equals(lastPredicateResult)) {
                    return true;
                }
                return next(request, LiPredicateSink.NO_NOT_OPERATION);
            }
        };
        return this;
    }

}
