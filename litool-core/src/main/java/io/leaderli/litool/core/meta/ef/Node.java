package io.leaderli.litool.core.meta.ef;

import io.leaderli.litool.core.meta.LiIf;
import io.leaderli.litool.core.meta.Lino;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 抽象节点，实现了LiIf接口，提供了一些方法实现
 *
 * @param <T> 节点类的泛型类型
 * @param <R> 节点返回值的类型*
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
    public LiThen<T, T, R> _if(Function<? super T, ?> filter) {

        return new PredicateNode<>(this, filter);
    }


    @Override
    public Lino<R> _else(Supplier<? extends R> supplier) {
        ElseNode<T, R> elseNode = new ElseNode<>(this, supplier);
        elseNode.subscribe(Subscription::request);
        return elseNode.getResult();
    }

}
