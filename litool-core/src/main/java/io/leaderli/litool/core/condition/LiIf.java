package io.leaderli.litool.core.condition;

import io.leaderli.litool.core.meta.LiBox;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.type.LiClassUtil;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author leaderli
 * @since 2022/6/23
 */
public interface LiIf<T, R> extends IfPublisher<T, R> {

    static <T, R> LiIf<T, R> of() {
        return of(null);
    }

    static <T, R> LiIf<T, R> of(T value) {
        return of(Lino.of(value));
    }

    static <T, R> LiIf<T, R> of(Lino<T> lino) {
        if (lino == null) {
            lino = Lino.none();
        }
        return new Begin<>(lino);
    }

    /**
     * @param predicate 断言函数
     * @return 返回一个可以提供 {@link LiThen#then(Function)} 转换函数的接口类，以方便链式调用。只有当 断言函数返回为true时，才会实际调用 转换函数
     * @see io.leaderli.litool.core.util.LiBoolUtil#parse(Object)
     */
    default LiThen<T, R> _if(Function<? super T, Object> predicate) {

        return new When<>(this, predicate);
    }

    /**
     * @param compare 当值 equals 时执行
     * @return {@link #_if(Function)}
     */
    default LiThen<T, R> _case(T compare) {
        return new When<>(this, v -> v.equals(compare));
    }

    /**
     * @param type 当  值 instanceof type 时执行
     * @return {@link #_if(Function)}
     */
    default LiThen<T, R> _instanceof(Class<?> type) {
        return new When<>(this, v -> LiClassUtil.isAssignableFromOrIsWrapper(type, v.getClass()));
    }

    /**
     * @param predicate 断言函数
     * @param mapping   转换函数
     * @return {@code _if(predicate).then(mapper)}
     * @see #_if(Function)
     * @see LiThen#then(Function)
     */
    default LiIf<T, R> _if(Function<? super T, Object> predicate, Function<? super T, ? extends R> mapping) {

        return _if(predicate).then(mapping);
    }

    /**
     * @param compare 当值 equals 时执行
     * @param mapping 转换函数
     * @return {@code _if(predicate).then(mapper)}
     * @see #_case(Object)
     * @see LiThen#then(Function)
     */
    default LiIf<T, R> _case(T compare, Function<? super T, ? extends R> mapping) {
        return _case(compare).then(mapping);
    }

    /**
     * @param type    当  值 instanceof type 时执行
     * @param mapping 转换函数
     * @return {@code _if(predicate).then(mapper)}
     * @see #_instanceof(Class)
     * @see LiThen#then(Function)
     */
    default LiIf<T, R> _instanceof(Class<?> type, Function<? super T, ? extends R> mapping) {
        return _instanceof(type).then(mapping);

    }

    /**
     * 当原数据为 null 时，则所有前置条件都不执行断言，直接使用默认值提供者
     *
     * @param supplier 当所有前置断言全部失败时的默认值提供者
     * @return 触发实际链条执行的函数
     */
    default Lino<R> _else(Supplier<? extends R> supplier) {
        Other<T, R> other = new Other<>(this, supplier);
        End<T, R> end = new End<>();
        return end.request(other);
    }

    /**
     * @param value 当所有前置断言全部失败时的默认值
     * @return {@link #_else(Supplier)}
     */
    default Lino<R> _else(R value) {
        return _else(() -> value);
    }

    class When<T, R> implements LiThen<T, R> {
        private final IfPublisher<T, R> prevPublisher;
        private final Function<? super T, Object> filter;

        public When(IfPublisher<T, R> prevPublisher, Function<? super T, Object> filter) {
            this.prevPublisher = prevPublisher;
            this.filter = filter;
        }

        @Override
        public void subscribe(IfSubscriber<T, R> actualSubscriber) {
            prevPublisher.subscribe(new WhenSubscriber<>(filter, actualSubscriber));

        }


    }

    class WhenSubscriber<T, R> extends IfMiddleSubscriber<T, R> {
        private final Function<? super T, Object> predicate;

        public WhenSubscriber(Function<? super T, Object> predicate, IfSubscriber<T, R> actualSubscriber) {
            super(actualSubscriber);
            this.predicate = predicate;

        }


        @Override
        public void next(T t, Function<? super T, Object> predicate) {

            this.actualSubscriber.next(t, this.predicate);
        }

    }

    class Other<T, R> implements LiThen<T, R> {
        private final IfPublisher<T, R> prevPublisher;
        private final Supplier<? extends R> supplier;

        public Other(IfPublisher<T, R> prevPublisher, Supplier<? extends R> supplier) {
            this.prevPublisher = prevPublisher;
            this.supplier = supplier;
        }

        @Override
        public void subscribe(IfSubscriber<T, R> actualSubscriber) {
            prevPublisher.subscribe(new OtherSubscriber<>(supplier, actualSubscriber));

        }
    }

    class OtherSubscriber<T, R> extends IfMiddleSubscriber<T, R> {
        private final Supplier<? extends R> supplier;

        public OtherSubscriber(Supplier<? extends R> supplier, IfSubscriber<T, R> actualSubscriber) {
            super(actualSubscriber);
            this.supplier = supplier;

        }


        @Override
        public void next(T t, Function<? super T, Object> predicate) {
            onComplete(supplier.get());
        }

    }

    /**
     * 用于开始响应式调用，并保存最终结果
     *
     * @param <T> 源数据泛型
     * @param <R> 结果数据泛型
     */
    class Begin<T, R> implements LiIf<T, R> {
        private final Lino<T> lino;

        public Begin(Lino<T> lino) {
            this.lino = lino;
        }

        @Override
        public void subscribe(IfSubscriber<T, R> subscriber) {

            subscriber.onSubscribe(new IfSubscription<R>() {


                private LiBox<R> box;

                @Override
                public void request(LiBox<R> box) {

                    this.box = box;
                    subscriber.next(lino.get(), null);
                }

                @Override
                public void onComplete(R value) {

                    this.box.value(value);
                }
            });
        }

    }

    class End<T, R> implements IfSubscriber<T, R> {

        private final LiBox<R> box = LiBox.none();

        private End() {
        }

        @Override
        public void onSubscribe(IfSubscription<R> subscription) {

            subscription.request(box);
        }

        @Override
        public void next(T t, Function<? super T, Object> predicate) {

            throw new UnsupportedOperationException();
        }

        public Lino<R> request(Other<T, R> other) {
            other.subscribe(this);
            return box.lino();
        }
    }
}

