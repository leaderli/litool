package io.leaderli.litool.core.meta.condition;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 该类与前一个节点配合使用
 *
 * @see LiIf
 */
public interface LiInstanceOfThen<T, M, R> extends PublisherIf<T, R> {


/**
 * @param mapping 转换函数，当条件满足时执行
 * @return 返回一个新的 LiIf ,  以方便链式调用
 */
default LiIf<T, R> then(Function<? super M, ? extends R> mapping) {

    return new CaseIf<>(this, mapping);
}

/**
 * @param supplier 当条件满足时提供值
 * @return 返回一个新的 LiIf ,  以方便链式调用
 */
default LiIf<T, R> then(Supplier<R> supplier) {

    return new CaseIf<>(this, v -> supplier.get());
}

/**
 * @param value 当条件满足时保存的值
 * @return 返回一个新的 LiIf ,  以方便链式调用
 */
default LiIf<T, R> then(R value) {

    return new CaseIf<>(this, v -> value);
}

/**
 * @param consumer 消费者，当条件满足时执行，不需要保存结构，会存储为 null
 * @return 返回一个新的 LiIf ,  以方便链式调用
 */
@SuppressWarnings("unchecked")
default LiIf<T, R> then(Consumer<? super M> consumer) {

    return new CaseIf<>(this, v -> {
        consumer.accept((M) v);
        return null;
    });
}


}
