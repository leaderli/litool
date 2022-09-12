package io.leaderli.litool.core.meta.logic;

import java.util.function.Function;

public class LiLogicPipeLine<T> implements CombineOperation<T>, UnionOperation<T> {

    private LogicSome<T> logic = new BeginSome<>();

    @SuppressWarnings("unchecked")
    public static <T> CombineOperation<T> begin() {
        LiLogicPipeLine<Object> objectLiLogicPipeLine = new LiLogicPipeLine<>();
        return (CombineOperation<T>) objectLiLogicPipeLine;
    }

    @Override
    public UnionOperation<T> test(Function<T, ?> predicate) {
        logic = new TestSome<>(logic, predicate);
        return this;
    }


    @Override
    public TestOperation<T> not() {
        logic = new NotSome<>(logic);
        return this;
    }


    @Override
    public CombineOperation<T> and() {
        logic = new AndSome<>(logic);
        return this;
    }

    @Override
    public CombineOperation<T> or() {
        logic = new OrSome<>(logic);
        return this;
    }


    @Override
    public Boolean apply(T t) {
        return logic.apply(t);
    }
}
