package io.leaderli.litool.core.meta.ef;

import io.leaderli.litool.core.meta.LiIf;
import io.leaderli.litool.core.meta.Lino;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author leaderli
 * @since 2022/9/6
 */
abstract class Node<T, R> implements LiIf<T, R> {


    @Override
    @SuppressWarnings("unchecked")
    public LiThen<T, T, R> _case(T... compares) {

        return new PredicateNode<>(this, v -> {

            if (compares != null) {
                for (T compare : compares) {
                    if (v.equals(compare)) {
                        return true;
                    }
                }
            }
            return false;
        });
    }


    @Override
    public <M extends T> LiThen<T, M, R> _instanceof(Class<? extends M> type) {
        return new InstanceOfNode<>(this, type);
    }


    @Override
    public LiThen<T, T, R> _if(Function<? super T, ?> predicate) {

        return new PredicateNode<>(this, predicate);
    }


    @Override
    public Lino<R> _else(Supplier<? extends R> supplier) {
        ElseNode<T, R> elseNode = new ElseNode<>(this, supplier);
        elseNode.subscribe(SubscriptionIf::request);
        return elseNode.getResult();
    }

}
