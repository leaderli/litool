package io.leaderli.litool.core.meta.condition;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 该类与前一个节点配合使用
 *
 * @see LiIf
 */
public interface LiThen<T, R> extends PublisherIf<T, R> {


    /**
     * @param mapping 转换函数，当条件满足时执行
     * @return 返回一个新的 LiIf ,  以方便链式调用
     */
    default LiIf<T, R> then(Function<? super T, ? extends R> mapping) {

        return new ThenIf<>(this, mapping);
    }

    /**
     * @param supplier 当条件满足时提供值
     * @return 返回一个新的 LiIf ,  以方便链式调用
     */
    default LiIf<T, R> then(Supplier<? extends R> supplier) {

        return new ThenIf<>(this, v -> supplier.get());
    }

    /**
     * @param value 当条件满足时保存的值
     * @return 返回一个新的 LiIf ,  以方便链式调用
     */
    default LiIf<T, R> then(R value) {

        return new ThenIf<>(this, v -> value);
    }

    /**
     * @param consumer 消费者，当条件满足时执行，不需要保存结构，会存储为 null
     * @return 返回一个新的 LiIf ,  以方便链式调用
     */
    default LiIf<T, R> then(Consumer<? super T> consumer) {

        return new ThenIf<>(this, v -> {
            consumer.accept(v);
            return null;
        });
    }


}
