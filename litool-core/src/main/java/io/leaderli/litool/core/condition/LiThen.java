package io.leaderli.litool.core.condition;

import io.leaderli.litool.core.util.LiBoolUtil;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 该类与前一个节点配合使用
 *
 * @see LiIf
 */
public interface LiThen<T, R> extends IfPublisher<T, R> {


    /**
     * @param mapping 转换函数，当条件满足时执行
     * @return 返回一个新的 LiIf ,  以方便链式调用
     */
    default LiIf<T, R> then(Function<? super T, ? extends R> mapping) {

        return new Then<>(this, mapping);
    }

    /**
     * @param supplier 当条件满足时提供值
     * @return 返回一个新的 LiIf ,  以方便链式调用
     */
    default LiIf<T, R> then(Supplier<R> supplier) {

        return new Then<>(this, v -> supplier.get());
    }

    /**
     * @param value 当条件满足时保存的值
     * @return 返回一个新的 LiIf ,  以方便链式调用
     */
    default LiIf<T, R> then(R value) {

        return new Then<>(this, v -> value);
    }

    /**
     * @param consumer 消费者，当条件满足时执行，不需要保存结构，会存储为 null
     * @return 返回一个新的 LiIf ,  以方便链式调用
     */
    default LiIf<T, R> then(Consumer<? super T> consumer) {

        return new Then<>(this, v -> {
            consumer.accept(v);
            return null;
        });
    }

    class Then<T, R> implements LiIf<T, R> {

        private final IfPublisher<T, R> prevPublisher;
        private final Function<? super T, ? extends R> mapper;


        public Then(IfPublisher<T, R> prevPublisher, Function<? super T, ? extends R> mapper) {
            this.prevPublisher = prevPublisher;
            this.mapper = mapper;
        }


        public void subscribe(IfSubscriber<T, R> actualSubscriber) {
            prevPublisher.subscribe(new ThenSubscriber<>(mapper, actualSubscriber));

        }

    }

    class ThenSubscriber<T, R> extends IfMiddleSubscriber<T, R> {
        private final Function<? super T, ? extends R> mapper;

        public ThenSubscriber(Function<? super T, ? extends R> mapper, IfSubscriber<T, R> actualSubscriber) {
            super(actualSubscriber);
            this.mapper = mapper;

        }


        /**
         * 对实际值进行断言，如果满足，值执行转换函数，并将结果保存，并终止执行，
         *
         * @param t         实际值
         * @param predicate 断言函数
         * @see #mapper
         */
        @Override
        public void next(T t, Function<? super T, ?> predicate) {

            if (t != null && LiBoolUtil.parse(predicate.apply(t))) {
                this.onComplete(this.mapper.apply(t));
            } else {
                this.actualSubscriber.next(t, null);
            }
        }

    }

}
