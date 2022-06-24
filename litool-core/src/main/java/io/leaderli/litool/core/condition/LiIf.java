package io.leaderli.litool.core.condition;

import io.leaderli.litool.core.meta.LiBox;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.type.LiClassUtil;

import java.util.function.Consumer;
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
     * @param compares 当存在 equals 的值时执行
     * @return {@link #_if(Function)}
     */
    @SuppressWarnings("unchecked")
    default LiThen<T, R> _case(T... compares) {

        return new When<>(this, v -> {

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
     * @param type 当  值 instanceof type 时执行
     * @return {@link #_if(Function)}
     */
    default LiThen<T, R> _instanceof(Class<?> type) {
        return new When<>(this, v -> LiClassUtil.isAssignableFromOrIsWrapper(type, v.getClass()));
    }

    /**
     * @param types 当  存在 instanceof types 的值时执行
     * @return {@link #_if(Function)}
     */
    default LiThen<T, R> _instanceof(Class<?>... types) {
        return new When<>(this, v -> {
            if (types != null) {

                for (Class<?> type : types) {

                    if (LiClassUtil.isAssignableFromOrIsWrapper(type, v.getClass())) {
                        return true;
                    }
                }
            }

            return false;
        });
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
     * 当原数据为 null 时，则所有前置条件都不执行断言，直接执行 runnable
     *
     * @param runnable 当所有前置断言全部失败时的时执行，该出恒返回 {@link Lino#none()}
     * @return 触发实际链条执行的函数
     */
    default Lino<R> _else(Runnable runnable) {
        Other<T, R> other = new Other<>(this, () -> {
            runnable.run();
            return null;
        });
        LiBox<R> result = LiBox.none();
        End<T, R> end = new End<>(result::value);
        end.request(other);
        return result.lino();
    }

    /**
     * 当原数据为 null 时，则所有前置条件都不执行断言，直接使用默认值提供者
     *
     * @param supplier 当所有前置断言全部失败时的默认值提供者
     * @return 触发实际链条执行的函数
     */
    default Lino<R> _else(Supplier<? extends R> supplier) {
        Other<T, R> other = new Other<>(this, supplier);
        LiBox<R> result = LiBox.none();
        End<T, R> end = new End<>(result::value);
        end.request(other);
        return result.lino();
    }

    /**
     * @param value 当所有前置断言全部失败时的默认值
     * @return {@link #_else(Supplier)}
     */
    default Lino<R> _else(R value) {
        return _else(() -> value);
    }

    /**
     * else 不做任何动作，仅用于触发函数调用
     *
     * @return {@link #_else(Supplier)} 传递参数为 null
     */
    default Lino<R> _else() {
        return _else((Supplier<? extends R>) null);
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
            if (supplier != null) {
                onComplete(supplier.get());
            }
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


                private Consumer<R> completeConsumer;

                @Override
                public void request(Consumer<R> completeConsumer) {

                    this.completeConsumer = completeConsumer;
                    subscriber.next(lino.get(), null);
                }

                @Override
                public void onComplete(R value) {
                    this.completeConsumer.accept(value);
                }
            });
        }

    }

    class End<T, R> implements IfSubscriber<T, R> {

        private final Consumer<R> completeConsumer;

        private End(Consumer<R> completeConsumer) {
            this.completeConsumer = completeConsumer;
        }

        @Override
        public void onSubscribe(IfSubscription<R> subscription) {

            subscription.request(completeConsumer);
        }

        @Override
        public void next(T t, Function<? super T, Object> predicate) {

            throw new UnsupportedOperationException();
        }

        public void request(Other<T, R> other) {
            other.subscribe(this);
        }
    }
}

