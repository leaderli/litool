package io.leaderli.litool.core.meta;

import io.leaderli.litool.core.exception.LiThrowableConsumer;
import io.leaderli.litool.core.exception.RuntimeExceptionTransfer;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author leaderli
 * @since 2022/6/16
 * <p>
 * 基于函数式编程思维，所有对其包含的value的值的操作，都是类型安全的。
 * <p>
 * 当且仅当value的值 {@link #isPresent()} 时，才会实际对其进行方法调用
 */
public interface Lino<T> extends LiValue {


    /**
     * @param value 值
     * @param <T>   泛型
     * @return 返回一个实例，
     * 当 {@code value == null} 时返回 {@link #none()}
     * 否则返回 {@link Some}
     */
    static <T> Lino<T> of(T value) {
        if (value == null) {
            return none();
        }
        return new Some<>(value);
    }

    /**
     * @param value 值
     * @param <T>   泛型
     * @return 窄化一个宽泛的泛型， {@code <? extends T> } 转换为  {@code  <T> }
     */
    @SuppressWarnings("unchecked")
    static <T> Lino<T> narrow(Lino<? extends T> value) {

        return (Lino<T>) value;

    }

    /**
     * @param <T> 泛型
     * @return {@link None#INSTANCE}
     */

    static <T> Lino<T> none() {
        //noinspection unchecked
        return (Lino<T>) None.INSTANCE;

    }

    default T getOrElse(T other) {

        return isPresent() ? get() : other;
    }

    T get();

    default T getOrElse(Supplier<? extends T> supplier) {

        return notPresent() && supplier != null ? supplier.get() : get();
    }

    /**
     * @param consumer 当 {@link #isPresent()}  时消费
     * @return this
     */
    Lino<T> ifPresent(Consumer<T> consumer);


    /**
     * @param consumer 当 {@link #isPresent()}  时消费，可能会抛出 {@link RuntimeException}
     * @return this
     * @see RuntimeExceptionTransfer
     */
    Lino<T> ifThrowablePresent(LiThrowableConsumer<T> consumer);


    /**
     * @param runnable 当 {@link #notPresent()}   时执行
     * @return this
     */
    Lino<T> ifNotPresent(Runnable runnable);

    class Some<T> implements Lino<T> {

        private final T value;

        public Some(T value) {
            this.value = value;
        }

        @Override
        public boolean isPresent() {
            return true;
        }

        @Override
        public String name() {
            return "Some";
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public Lino<T> ifPresent(Consumer<T> consumer) {
            consumer.accept(this.value);
            return this;
        }

        @Override
        public Lino<T> ifThrowablePresent(LiThrowableConsumer<T> consumer) {
            RuntimeExceptionTransfer.accept(consumer, this.value);
            return this;
        }

        @Override
        public Lino<T> ifNotPresent(Runnable runnable) {
            return this;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Some<?> some = (Some<?>) o;
            return Objects.equals(value, some.value);
        }

        @Override
        public String toString() {
            return name() + "(" + value + ")";
        }
    }

    class None<T> implements Lino<T> {
        private static final None<?> INSTANCE = new None<>();

        private None() {
        }


        @Override
        public boolean isPresent() {
            return false;
        }

        @Override
        public String name() {
            return "None";
        }

        @Override
        public String toString() {
            return name() + "()";
        }

        @Override
        public T get() {
            return null;
        }

        @Override
        public Lino<T> ifPresent(Consumer<T> consumer) {
            return this;
        }

        @Override
        public Lino<T> ifThrowablePresent(LiThrowableConsumer<T> consumer) {
            return this;
        }

        @Override
        public Lino<T> ifNotPresent(Runnable runnable) {
            runnable.run();
            return this;
        }
    }
}
