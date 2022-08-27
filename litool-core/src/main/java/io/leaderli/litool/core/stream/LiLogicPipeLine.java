package io.leaderli.litool.core.stream;

import java.util.function.Predicate;

public class LiLogicPipeLine<T> implements InterLogicPipeLineSink<T> {


private Sink<T, Boolean> sink;

private LiLogicPipeLine() {

}

public static <T> InterCombineOperationSink<T> begin() {

    LiLogicPipeLine<T> logic = new LiLogicPipeLine<>();
    logic.sink = new Head<>();
    return logic;
}

@Override
public Boolean apply(T t) {
    return sink.request(t);
}

@Override
public InterOperationSink<T> not() {
    this.sink = new Sink<T, Boolean>(this.sink) {
        @Override
        public Boolean apply(T request, Boolean last) {
            return next(request, PredicateSink.IS_NOT_OPERATION);
        }
    };
    return this;
}

@Override
public InterPredicateSink<T> test(Predicate<T> predicate) {

    this.sink = new PredicateSink<>(this.sink, predicate);

    return this;
}

@Override
public InterCombineOperationSink<T> and() {

    this.sink = new Sink<T, Boolean>(this.sink) {
        @Override
        public Boolean apply(T request, Boolean lastPredicateResult) {
            //短路
            if (Boolean.TRUE.equals(lastPredicateResult)) {
                return next(request, PredicateSink.NO_NOT_OPERATION);
            }
            return false;
        }
    };
    return this;
}

@Override
public InterCombineOperationSink<T> or() {
    this.sink = new Sink<T, Boolean>(this.sink) {
        @Override
        public Boolean apply(T request, Boolean lastPredicateResult) {
            //短路
            if (Boolean.TRUE.equals(lastPredicateResult)) {
                return true;
            }
            return next(request, PredicateSink.NO_NOT_OPERATION);
        }
    };
    return this;
}

private static class Head<T> extends Sink<T, Boolean> {

    public Head() {
        super(null);
    }

    @Override
    public Boolean apply(T request, Boolean last) {
        return next(request, PredicateSink.NO_NOT_OPERATION);
    }
}

}
